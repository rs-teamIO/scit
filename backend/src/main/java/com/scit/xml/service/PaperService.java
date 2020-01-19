package com.scit.xml.service;

import com.scit.xml.model.paper.Paper;
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

        return this.paperRepository.save(paper);
    }
}
