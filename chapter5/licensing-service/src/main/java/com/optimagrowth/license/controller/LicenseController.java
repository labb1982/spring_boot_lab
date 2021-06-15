package com.optimagrowth.license.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.optimagrowth.license.model.License;
import com.optimagrowth.license.service.LicenseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping(value="v1/organization/{organizationId}/license")
public class LicenseController {

	@Autowired
	private LicenseService licenseService;

	//https://stackoverflow.com/questions/59837566/unable-to-show-custom-header-in-open-api-ui
	@Operation(summary = "Find Contacts by name", description = "Name search by %name% format", tags = { "contact" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = License.class)))) })
	@Parameter(in = ParameterIn.HEADER, description = "Custom Header To be Pass", name = "Accept-version", schema = @Schema(type = "string", defaultValue = "v1", allowableValues = {
			"v1", "v2" }, implementation = License.class)
//	content = @Content(schema = @Schema(type = "string", defaultValue = "v1", allowableValues = {		"v1", "v2" }, implementation = License.class))
	)
    @RequestMapping(value="/{licenseId}",method = RequestMethod.GET)
	public ResponseEntity<License> getLicense( @PathVariable("organizationId") final String organizationId,
			@PathVariable("licenseId") final String licenseId) {
		
		final License license = licenseService.getLicense(licenseId, organizationId);
		license.add( 
				linkTo(methodOn(LicenseController.class).getLicense(organizationId, license.getLicenseId())).withSelfRel(),
				linkTo(methodOn(LicenseController.class).createLicense(license)).withRel("createLicense"),
				linkTo(methodOn(LicenseController.class).updateLicense(license)).withRel("updateLicense"),
				linkTo(methodOn(LicenseController.class).deleteLicense(license.getLicenseId())).withRel("deleteLicense")
		);
		
		return ResponseEntity.ok(license);
	}

	@PutMapping
	public ResponseEntity<License> updateLicense(@RequestBody final License request) {
		return ResponseEntity.ok(licenseService.updateLicense(request));
	}
	
    	@Parameter(in = ParameterIn.HEADER, description = "Locale Desc", name = "Accept-Language", schema = @Schema(type = "string"//, defaultValue = "en"
	// ,allowableValues = { "v1", "v2" }, implementation = License.class
	))

	@Operation(summary = "License Create", description = "Create License", tags = { "License" })
	@PostMapping
	public ResponseEntity<License> createLicense(@RequestBody final License request) {
		return ResponseEntity.ok(licenseService.createLicense(request));
	}

	@DeleteMapping(value="/{licenseId}")
	public ResponseEntity<String> deleteLicense(@PathVariable("licenseId") final String licenseId) {
		return ResponseEntity.ok(licenseService.deleteLicense(licenseId));
	}
}
