/*
 * FHIRWork (c) 2018 - Blair Butterworth, Abdul-Qadir Ali, Xialong Chen,
 * Chenghui Fan, Alperen Karaoglu, Jiaming Zhou
 *
 * This work is licensed under the MIT License. To view a copy of this
 * license, visit
 *
 *      https://opensource.org/licenses/MIT
 */

package org.ucl.fhirwork.network.fhir.servlet;

import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.ucl.fhirwork.mapping.ExecutorService;
import org.ucl.fhirwork.network.fhir.operations.observation.ReadObservationOperation;

import javax.inject.Inject;
import java.util.List;

/**
 * Instances of this class provide implement functions defined in the FHIR
 * specification related to Observations. Once implemented these operation can
 * be then be called by FHIR clients.
 *
 * @author Blair Butterworth
 */
@SuppressWarnings({"unused", "unchecked"})
public class ObservationResourceProvider implements IResourceProvider
{
    private ExecutorService executorService;

    @Inject
    public ObservationResourceProvider(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return Observation.class;
    }

    @Search
    public List<Observation> searchByPatient(
        @OptionalParam(name = Observation.SP_CODE) TokenOrListParam codes,
        @RequiredParam(name = Observation.SP_PATIENT) ReferenceParam patient)
    {
        return search(codes, patient);
    }

    @Search
    public List<Observation> searchBySubject(
        @OptionalParam(name = Observation.SP_CODE) TokenOrListParam codes,
        @RequiredParam(name = Observation.SP_SUBJECT) ReferenceParam subject)
    {
        return search(codes, subject);
    }

    private List<Observation> search(TokenOrListParam codes, ReferenceParam subject)
    {
        try {
            codes = codes != null ? codes : new TokenOrListParam();
            ReadObservationOperation operation = new ReadObservationOperation(codes, subject);
            return (List<Observation>)executorService.execute(operation);
        }
        catch (Throwable error) {
            throw new InternalErrorException(error);
        }
    }
}
