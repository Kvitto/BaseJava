package com.urise.webapp.storage.serializer;

import com.urise.webapp.model.*;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class DataStreamSerializer implements StreamSerializer {
    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());
            writeWithException(r.getContacts().entrySet(), dos, contact -> {
                dos.writeUTF(contact.getKey().name());
                dos.writeUTF(contact.getValue());
            });
            dos.writeInt(r.getSections().size());
            for (Map.Entry<SectionType, Section> entry : r.getSections().entrySet()) {
                SectionType sectionType = entry.getKey();
                Section section = entry.getValue();
                dos.writeUTF(entry.getKey().name());
                switch (sectionType) {
                    case PERSONAL, OBJECTIVE -> dos.writeUTF(((TextSection) section).getContent());
                    case ACHIEVEMENT, QUALIFICATION ->
                            writeWithException(((ListSection) section).getItems(), dos, dos::writeUTF);
                    case EXPERIENCE, EDUCATION -> serializeCompany(dos, section);
                }
            }
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);
            deserializeString(dis, () -> resume.addContact(ContactType.valueOf(dis.readUTF()), dis.readUTF()));
            int size = dis.readInt();
            for (int i = 0; i < size; i++) {
                SectionType sectionType = SectionType.valueOf(dis.readUTF());
                switch (sectionType) {
                    case PERSONAL, OBJECTIVE -> resume.addSections(sectionType, new TextSection(dis.readUTF()));
                    case ACHIEVEMENT, QUALIFICATION ->
                            resume.addSections(sectionType, new ListSection(deserializeList(dis, dis::readUTF)));
                    case EXPERIENCE, EDUCATION ->
                        resume.addSections(sectionType, new CompanySection(deserializeList(dis, () -> {
                            Company company = new Company(dis.readUTF(), dis.readUTF(), new ArrayList<>());
                            company.setPositions(deserializeList(dis, () -> {
                                Company.Position position;
                                position = new Company.Position(
                                        LocalDate.parse(dis.readUTF(), DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                                        LocalDate.parse(dis.readUTF(), DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                                        dis.readUTF(), dis.readUTF());
                                return position;
                            }));
                            return company;
                        })));
                }
            }
            return resume;
        }
    }

    private void serializeCompany(DataOutputStream dos, Section section) throws IOException {
        writeWithException(((CompanySection) section).getCompanies(), dos, company -> {
            dos.writeUTF(company.getWebsite().getName());
            dos.writeUTF(company.getWebsite().getUrl());
            writeWithException(company.getPositions(), dos, position -> {
                dos.writeUTF(position.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                dos.writeUTF(position.getEndDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                dos.writeUTF(position.getTitle());
                dos.writeUTF(position.getDescription());
            });
        });
    }

    private <T> void writeWithException(Collection<T> collection, DataOutputStream dos, Writer<T> writer) throws IOException {
        dos.writeInt(collection.size());
        for (T c : collection) {
            writer.write(c);
        }
    }

    private void deserializeString(DataInputStream dis, ContactsReader contactsReader) throws IOException {
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            contactsReader.read();
        }
    }

    private <T> List<T> deserializeList(DataInputStream dis, Reader<T> reader) throws IOException {
        List<T> list = new ArrayList<>();
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            list.add(reader.read());
        }
        return list;
    }

    private interface Writer<T> {
        void write(T t) throws IOException;
    }

    private interface ContactsReader {
        void read() throws IOException;
    }

    private interface Reader<T> {
        T read() throws IOException;
    }
}