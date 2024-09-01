package com.urise.webapp.storage.serializer;

import com.urise.webapp.model.*;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataStreamSerializer implements StreamSerializer {
    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());
            serializeContacts(dos, r);
            dos.writeInt(r.getSections().size());
            for (Map.Entry<SectionType, Section> entry : r.getSections().entrySet()) {
                switch (SectionType.valueOf(entry.getKey().name())) {
                    case PERSONAL, OBJECTIVE -> serializeText(dos, entry);
                    case ACHIEVEMENT, QUALIFICATION -> serializeList(dos, entry);
                    case EXPERIENCE, EDUCATION -> serializeCompany(dos, entry);
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
            deserializeContacts(dis, resume);
            int size = dis.readInt();
            for (int i = 0; i < size; i++) {
                SectionType sectionType = SectionType.valueOf(dis.readUTF());
                switch (sectionType) {
                    case PERSONAL, OBJECTIVE -> resume.addSections(sectionType, new TextSection(dis.readUTF()));
                    case ACHIEVEMENT, QUALIFICATION ->
                            resume.addSections(sectionType, new ListSection(deserializeSection(dis, String.class)));
                    case EXPERIENCE, EDUCATION ->
                            resume.addSections(sectionType, new CompanySection(deserializeSection(dis, Company.class)));
                }
            }
            return resume;
        }
    }

    private void serializeContacts(DataOutputStream dos, Resume resume) throws IOException {
        dos.writeInt(resume.getContacts().size());
        for (Map.Entry<ContactType, String> entry : resume.getContacts().entrySet()) {
            dos.writeUTF(entry.getKey().name());
            dos.writeUTF(entry.getValue());
        }
    }

    private void serializeText(DataOutputStream dos, Map.Entry<SectionType, Section> entry) throws IOException {
        dos.writeUTF(entry.getKey().name());
        TextSection textSection = (TextSection) entry.getValue();
        dos.writeUTF(textSection.getContent());
    }

    private void serializeList(DataOutputStream dos, Map.Entry<SectionType, Section> entry) throws IOException {
        dos.writeUTF(entry.getKey().name());
        List<String> list = ((ListSection) entry.getValue()).getItems();
        dos.writeInt(list.size());
        for (String string : list) {
            dos.writeUTF(string);
        }
    }

    private void serializeCompany(DataOutputStream dos, Map.Entry<SectionType, Section> entry) throws IOException {
        dos.writeUTF(entry.getKey().name());
        List<Company> list = ((CompanySection) entry.getValue()).getCompanies();
        dos.writeInt(list.size());
        for (Company company : list) {
            dos.writeUTF(company.getWebsite().getName());
            dos.writeUTF(company.getWebsite().getUrl());
            dos.writeInt(company.getPositions().size());
            for (Company.Position position : company.getPositions()) {
                dos.writeUTF(position.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                dos.writeUTF(position.getEndDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                dos.writeUTF(position.getTitle());
                dos.writeUTF(position.getDescription());
            }
        }
    }

    private void deserializeContacts(DataInputStream dis, Resume resume) throws IOException {
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            resume.addContact(ContactType.valueOf(dis.readUTF()), dis.readUTF());
        }
    }

    private <T> List<T> deserializeSection(DataInputStream dis, Class<T> tClass) throws IOException {
        List<T> list = new ArrayList<>();
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            if (tClass.isAssignableFrom(String.class)) {
                list.add(tClass.cast(dis.readUTF()));
            } else {
                Company company = new Company(dis.readUTF(), dis.readUTF(), new ArrayList<>());
                int positionSize = dis.readInt();
                for (int j = 0; j < positionSize; j++) {
                    company.addPosition(new Company.Position(
                            LocalDate.parse(dis.readUTF(), DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                            LocalDate.parse(dis.readUTF(), DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                            dis.readUTF(), dis.readUTF()));
                }
                list.add(tClass.cast(company));
            }
        }
        return list;
    }
}