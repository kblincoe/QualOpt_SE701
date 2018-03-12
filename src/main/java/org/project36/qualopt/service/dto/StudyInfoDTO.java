package org.project36.qualopt.service.dto;

import org.project36.qualopt.domain.Study;
import org.project36.qualopt.domain.Document;
import java.util.Set;

public class StudyInfoDTO {

    private String name;

    private String faq;

    private Set<Document> documents;

    public StudyInfoDTO(Study study) {
        this.name = study.getName();
        this.faq = study.getFaq();
        this.documents = study.getDocuments();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFaq() {
        return this.faq;
    }

    public void setFaq(String faq) {
        this.faq = faq;
    }

    public Set<Document> getDocuments() {
        return this.documents;
    }

    public void setDocuments(Set<Document> documents) {
        this.documents = documents;
    }
}