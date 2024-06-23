package org.webCrawler.api.Codal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.webCrawler.common.CommonUtils;
import org.webCrawler.common.LettersTypes;
import org.webCrawler.dto.InterimStatementDto;
import org.webCrawler.model.IncomeStatement;
import org.webCrawler.service.MeetingService;
import org.webCrawler.service.MongoGenericService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class FinancialStatementsAPI {

    @Autowired
    MeetingService meetingService;
    @Autowired
    MongoGenericService<InterimStatementDto> interimStatementService;

    @GetMapping(path = "/api/interimStatementDto")
    public List<InterimStatementDto> getInterimStatementDto(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        CommonUtils.checkInputData(startDate, endDate);
        List<InterimStatementDto> interimStatementDtos = new ArrayList<>();
        interimStatementDtos.addAll(meetingService.getInterimStatementDto(LettersTypes.INFORMATION_AND_INTERIM_FINANCIAL_STATEMENTS.getLettersTypeValue().toString(), startDate, endDate));
        for (InterimStatementDto item : interimStatementDtos) {
            interimStatementService.add(item);
        }
        return interimStatementDtos;
    }

    @GetMapping(path = "/api/getIncomeStatement")
    public List<IncomeStatement> getIncomeStatement() throws Exception {
        List<IncomeStatement> incomeStatements = new ArrayList<>();
        meetingService.saveIncomeStatement();
        return incomeStatements;

    }
}
