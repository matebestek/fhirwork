/*
 * FHIRWork (c) 2018 - Blair Butterworth, Abdul-Qadir Ali, Xialong Chen,
 * Chenghui Fan, Alperen Karaoglu, Jiaming Zhou
 *
 * This work is licensed under the MIT License. To view a copy of this
 * license, visit
 *
 *      https://opensource.org/licenses/MIT
 */

package org.ucl.fhirwork.mapping.executor.patient;

import ca.uhn.fhir.model.dstu2.resource.Patient;
import org.apache.commons.lang3.Validate;
import org.ucl.fhirwork.common.framework.ExecutionException;
import org.ucl.fhirwork.common.framework.Executor;
import org.ucl.fhirwork.common.framework.Operation;
import org.ucl.fhirwork.mapping.data.PatientFactory;
import org.ucl.fhirwork.mapping.data.PersonFactory;
import org.ucl.fhirwork.network.NetworkService;
import org.ucl.fhirwork.network.empi.data.Person;
import org.ucl.fhirwork.network.empi.server.EmpiServer;
import org.ucl.fhirwork.network.fhir.data.SearchParameter;
import org.ucl.fhirwork.network.fhir.operations.patient.CreatePatientOperation;

import javax.inject.Inject;
import java.util.Map;

/**
 * Instances of this class convert the conditional create patient FHIR
 * operation into the appropriate EMPI service calls.
 *
 * @author Alperen Karaoglu
 * @author Blair Butterworth
 */
public class CreatePatientConditionalExecutor implements Executor
{
    private EmpiServer empiServer;
    private PatientFactory patientFactory;
    private PersonFactory personFactory;
    private Map<SearchParameter, Object> searchParameters;

    @Inject
    public CreatePatientConditionalExecutor(
            NetworkService networkService,
            PatientFactory patientFactory,
            PersonFactory personFactory)
    {
        this.empiServer = networkService.getEmpiServer();
        this.patientFactory = patientFactory;
        this.personFactory = personFactory;
    }

    @Override
    public void setOperation(Operation operation)
    {
        Validate.notNull(operation);
        CreatePatientOperation createPatient = (CreatePatientOperation)operation;
        searchParameters = createPatient.getSearchParameters();
    }

    @Override
    public Object invoke() throws ExecutionException
    {
        try {
            Person personInput = personFactory.fromSearchParameters(searchParameters);
            Person personOutput = empiServer.addPerson(personInput);
            return patientFactory.fromPerson(personOutput);
        }
        catch (Throwable cause){
            throw new ExecutionException(cause);
        }
    }
}
