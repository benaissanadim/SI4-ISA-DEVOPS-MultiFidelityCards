package fr.univcotedazur.multifidelitycards.controllers;

import fr.univcotedazur.multifidelitycards.components.SurveyService;
import fr.univcotedazur.multifidelitycards.controllers.dto.*;
import fr.univcotedazur.multifidelitycards.controllers.mapper.QuestionMapper;
import fr.univcotedazur.multifidelitycards.controllers.mapper.SurveyMapper;
import fr.univcotedazur.multifidelitycards.entities.Question;
import fr.univcotedazur.multifidelitycards.exceptions.NoSurveyFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * SurveyController responsible for survey management
 */
@RestController
@RequestMapping(path = SurveyController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class SurveyController {
    public static final String BASE_URI = "/surveys";

    @Autowired
    private SurveyService surveyService;
    @Autowired
    private SurveyMapper mapper;
    @Autowired
    private QuestionMapper questionMapper;


    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler({ MethodArgumentNotValidException.class})
    public ErrorDTO handleExceptions(MethodArgumentNotValidException e) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError("Cannot process survey information");
        errorDTO.setDetails(e.getMessage());
        return errorDTO;
    }

    @PostMapping(consumes = {APPLICATION_JSON_VALUE})
    public ResponseEntity<SurveyDTO> create(@RequestBody @Valid SurveyDTO surveyDTO) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).
                    body(mapper.toDto(surveyService.createSurvey(surveyDTO.getName(), questionMapper.toList(surveyDTO.getQuestions()))));
        } catch (Exception var3) {
            System.out.println(var3.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping(path = "{surveyId}/question/{questionId}/reply/{clientId}")
    public ResponseEntity<QuestionDTO> respondToSurvey(
            @PathVariable Long surveyId,
            @PathVariable Long questionId,
            @PathVariable Long clientId,
            @RequestBody String answer) {
        try {
            Question q = surveyService.respondToQuestion(surveyId, questionId, answer,clientId);
            return ResponseEntity.ok(questionMapper.toDto(q));
        } catch (NoSurveyFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "{surveyId}")
    public ResponseEntity<SurveyDTO> viewSurvey(@PathVariable Long surveyId) {
        try {
            return ResponseEntity.ok(mapper.toDto(surveyService.viewSurvey(surveyId)));
        } catch (NoSurveyFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(path = "{surveyId}")
    public ResponseEntity<String> deleteSurvey(@PathVariable Long surveyId) {
        try {
            surveyService.deleteSurvey(surveyId);
            return ResponseEntity.ok().build();
        } catch (NoSurveyFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
