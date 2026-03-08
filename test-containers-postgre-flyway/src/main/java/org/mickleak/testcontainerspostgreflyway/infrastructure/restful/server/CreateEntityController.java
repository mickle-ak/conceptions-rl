package org.mickleak.testcontainerspostgreflyway.infrastructure.restful.server;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.mickleak.testcontainerspostgreflyway.application.CreateEntityUseCase;
import org.mickleak.testcontainerspostgreflyway.domain.Entity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class CreateEntityController {

    private final CreateEntityUseCase createEntityUseCase;

    public CreateEntityController(CreateEntityUseCase createEntityUseCase) {
        this.createEntityUseCase = createEntityUseCase;
    }


    @PostMapping("/entity")
    @PreAuthorize("hasAuthority('entity_edit')")
    @Operation(summary = "To create a new entity", description = "Creates new entity")
    @ApiResponse(responseCode = "200", description = "Entity created", content = @Content(schema = @Schema(implementation = CreateEntityResponse.class)))
    public ResponseEntity<CreateEntityResponse> createEntity(@RequestBody CreateEntityRequest request) {
        Entity createdEntity = createEntityUseCase.execute(request.getName());
        return ResponseEntity.ok(new CreateEntityResponse(createdEntity));
    }
}
