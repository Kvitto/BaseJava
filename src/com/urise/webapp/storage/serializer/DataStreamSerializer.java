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
            Map<SectionType, Section> sections = r.getSections();
            dos.writeInt(sections.size());
            for (Map.Entry<SectionType, Section> entry : sections.entrySet()) {
                SectionType sectionType = SectionType.valueOf(entry.getKey().name());
                Section section = entry.getValue();
                switch (sectionType) {
                    case PERSONAL, OBJECTIVE -> serializeText(dos, sectionType, section);
                    case ACHIEVEMENT, QUALIFICATION -> serializeList(dos, sectionType, section);
                    case EXPERIENCE, EDUCATION -> serializeCompany(dos, sectionType, section);
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
                            resume.addSections(sectionType, new ListSection(deserializeList(dis)));
                    case EXPERIENCE, EDUCATION ->
                            resume.addSections(sectionType, new CompanySection(deserializeCompany(dis)));
                }
            }
            return resume;
        }
    }

    private void serializeContacts(DataOutputStream dos, Resume resume) throws IOException {
        Map<ContactType, String> contacts = resume.getContacts();
        dos.writeInt(contacts.size());
        for (Map.Entry<ContactType, String> entry : resume.getContacts().entrySet()) {
            dos.writeUTF(entry.getKey().name());
            dos.writeUTF(entry.getValue());
        }
    }

    private void serializeText(DataOutputStream dos, SectionType type, Section section) throws IOException {
        dos.writeUTF(type.name());
        TextSection textSection = (TextSection) section;
        dos.writeUTF(textSection.getContent());
    }

    private void serializeList(DataOutputStream dos, SectionType type, Section section) throws IOException {
        dos.writeUTF(type.name());
        List<String> list = ((ListSection) section).getItems();
        dos.writeInt(list.size());
        for (String string : list) {
            dos.writeUTF(string);
        }
    }

    private void serializeCompany(DataOutputStream dos, SectionType type, Section section) throws IOException {
        dos.writeUTF(type.name());
        List<Company> list = ((CompanySection) section).getCompanies();
        dos.writeInt(list.size());
        for (Company company : list) {
            dos.writeUTF(company.getWebsite().getName());
            dos.writeUTF(company.getWebsite().getUrl());
            List<Company. Position> positions = company.getPositions();
            dos.writeInt(positions.size());
            for (Company.Position position : positions) {
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

    private List<String> deserializeList(DataInputStream dis) throws IOException {
        List<String> list = new ArrayList<>();
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            list.add(dis.readUTF());
        }
        return list;
    }

    private List<Company> deserializeCompany(DataInputStream dis) throws IOException {
        List<Company> list = new ArrayList<>();
        int companySize = dis.readInt();
        for (int i = 0; i < companySize; i++) {
            Company company = new Company(dis.readUTF(), dis.readUTF(), new ArrayList<>());
            int positionSize = dis.readInt();
            for (int j = 0; j < positionSize; j++) {
                company.addPosition(new Company.Position(
                        LocalDate.parse(dis.readUTF(), DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        LocalDate.parse(dis.readUTF(), DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        dis.readUTF(), dis.readUTF()));
            }
            list.add(company);
        }
        return list;
    }
}