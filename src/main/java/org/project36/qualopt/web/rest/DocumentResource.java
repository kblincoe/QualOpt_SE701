package org.project36.qualopt.web.rest;

import javax.persistence.EntityNotFoundException;

import com.codahale.metrics.annotation.Timed;

import org.apache.commons.codec.binary.Base64;
import org.project36.qualopt.domain.Document;
import org.project36.qualopt.repository.DocumentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing Email.
 */
@RestController
@RequestMapping("/api")
public class DocumentResource {

    private final Logger log = LoggerFactory.getLogger(DocumentResource.class);

    private final DocumentRepository documentRepository;

    public DocumentResource(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @RequestMapping("/documents/{id}/download")
    @Timed
    public ResponseEntity<Resource> downloadDocument(@PathVariable Long id) {
        Document document;
        
        try {
            document = documentRepository.getOne(id);
        } 
        catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        byte[] fileBlob = Base64.decodeBase64(document.getFileimage());
        ByteArrayResource resource = new ByteArrayResource(fileBlob);

        return ResponseEntity.ok()
            .header("Content-Disposition", String.format("inline; filename=\"" + document.getFilename() + "\""))
            .contentLength(fileBlob.length)
            .contentType(MediaType.parseMediaType("application/octet-stream"))
            .body(resource);
    }

}
