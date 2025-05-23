package br.com.neurotech.challenge.controller;

import br.com.neurotech.challenge.dtos.ClientDto;
import br.com.neurotech.challenge.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/client")
public class ClientController {

    private final ClientService service;

    public ClientController(ClientService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Create a client", description = "Add a new Neurotech client to the system",
            tags = {"Neurotech Clients"}, responses = {
            @ApiResponse(description = "Created", responseCode = "201",
                    content = @Content(schema = @Schema(implementation = ClientDto.class))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = {@Content}),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = {@Content})
    })
    public ResponseEntity<ClientDto> createClient(@Valid @RequestBody ClientDto client) {
        ClientDto createdClient = service.create(client);
        Map<String, String> hateoasLinks = service.getHateoasLinks(createdClient.getId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Location", hateoasLinks.get("self"))
                .body(createdClient);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find a client by ID", description = "Retrieve client details by their ID",
            tags = {"Neurotech Clients"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = ClientDto.class))),
            @ApiResponse(description = "Not Found", responseCode = "404", content = {@Content}),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = {@Content})
    })
    public ResponseEntity<ClientDto> getClientById(@PathVariable Long id) {
        ClientDto clientDto = service.findById(id);
        Map<String, String> hateoasLinks = service.getHateoasLinks(id);

        return ResponseEntity.ok()
                .header("Location", hateoasLinks.get("self"))
                .body(clientDto);
    }


    @GetMapping
    @Operation(summary = "Find all clients", description = "Retrieve a list of all Neurotech clients",
            tags = {"Neurotech Clients"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ClientDto.class)))) ,
            @ApiResponse(description = "Internal Error", responseCode = "500", content = {@Content})
    })
    public ResponseEntity<List<ClientDto>> getAllClients() {
        List<ClientDto> clients = service.findAll();
        return ResponseEntity.ok()
                .header("Location", linkToSelfList())
                .body(clients);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a client", description = "Update details of an existing Neurotech client",
            tags = {"Neurotech Clients"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = ClientDto.class))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = {@Content}),
            @ApiResponse(description = "Not Found", responseCode = "404", content = {@Content}),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = {@Content})
    })
    public ResponseEntity<ClientDto> updateClient(
            @PathVariable Long id,
            @Valid @RequestBody ClientDto client) {

        ClientDto updatedClient = service.update(id, client);
        Map<String, String> hateoasLinks = service.getHateoasLinks(id);

        return ResponseEntity.ok()
                .header("Location", hateoasLinks.get("self"))
                .body(updatedClient);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a client by ID", description = "Remove a Neurotech client from the system by their ID",
            tags = {"Neurotech Clients"}, responses = {
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = {@Content}),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = {@Content})
    })
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        service.delete(id);
        Map<String, String> hateoasLinks = service.getHateoasLinks(id);

        return ResponseEntity.noContent()
                .header("Location", hateoasLinks.get("self"))
                .build();
    }

    private String linkToSelfList() {
        return "/api/neurotech-clients";
    }
}
