package com.scit.xml.service;

import com.scit.xml.model.Paper;
import com.scit.xml.repository.PaperRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaperService {

    private final PaperRepository paperRepository;
    // private final PaperDatabaseValidator paperDatabaseValidator;

    public String createPaper(Paper paper) {
        // TODO: DB Validation
        // this.paperDatabaseValidator.validate(paperDto);

        // TODO: Give paper object as parameter
        String paperId = this.paperRepository.save(paper);

        return paperId;
    }
}
