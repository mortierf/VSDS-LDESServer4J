package be.vlaanderen.informatievlaanderen.ldes.server.ingest.rest;

import be.vlaanderen.informatievlaanderen.ldes.server.ingest.MemberIngester;
import be.vlaanderen.informatievlaanderen.ldes.server.ingest.entities.Member;
import be.vlaanderen.informatievlaanderen.ldes.server.ingest.rest.validators.CollectionNameValidator;
import io.micrometer.observation.annotation.Observed;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Observed
@RestController
public class MemberIngestController implements OpenApiMemberIngestController {

	private final CollectionNameValidator collectionNameValidator;
	private final MemberIngester memberIngester;

	public MemberIngestController(CollectionNameValidator collectionNameValidator, MemberIngester memberIngester) {
        this.collectionNameValidator = collectionNameValidator;
        this.memberIngester = memberIngester;
	}

	@Override
	@PostMapping(value = "{collectionName}")
	public ResponseEntity<Object> ingestLdesMember(@RequestBody Member member, @PathVariable String collectionName) {
		collectionNameValidator.validate(collectionName);
		HttpStatus statusCode = memberIngester.ingest(member) ? HttpStatus.CREATED : HttpStatus.OK;
		return new ResponseEntity<>(statusCode);
	}

}
