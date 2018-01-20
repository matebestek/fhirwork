/*
 * FHIRWork (c)
 *
 * This work is licensed under the Creative Commons Attribution 4.0
 * International License. To view a copy of this license, visit
 *
 *      http://creativecommons.org/licenses/by/4.0/
 */

package org.ucl.fhirwork.integration.fhir;

import com.google.common.collect.ImmutableMap;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;
import org.ucl.fhirwork.integration.fhir.model.Bundle;
import org.ucl.fhirwork.integration.fhir.model.BundleEntry;
import org.ucl.fhirwork.integration.fhir.model.FhirPatient;
import org.ucl.fhirwork.integration.serialization.JsonSerializer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class FhirServer
{
    private static final String ACCEPT_JSON = "application/json";
    private static final String ACCEPT_HEADER = "accept";

    private static final String GENDER_PARAMETER = "gender";
    private static final String FAMILY_PARAMETER = "family";
    private static final String IDENTIFIER_PARAMETER = "identifier";

    private static final String PATIENT_ENDPOINT = "fhir/Patient";

    private String server;
    private JsonSerializer serializer;

    public FhirServer(String server)
    {
        this.server = server.endsWith("/") ? server : server + "/";
        this.serializer = new JsonSerializer();
    }

    public List<FhirPatient> searchPatients() throws FhirServerException
    {
        Bundle bundle = get(PATIENT_ENDPOINT, Bundle.class, Collections.emptyMap());
        return getPatients(bundle);
    }

    public List<FhirPatient> searchPatientsByIdentifier(String identifier) throws FhirServerException
    {
        Bundle bundle = get(PATIENT_ENDPOINT, Bundle.class, ImmutableMap.of(IDENTIFIER_PARAMETER, identifier));
        return getPatients(bundle);
    }

    public List<FhirPatient> searchPatientsByGender(String gender) throws FhirServerException
    {
        Bundle bundle = get(PATIENT_ENDPOINT, Bundle.class, ImmutableMap.of(GENDER_PARAMETER, gender));
        return getPatients(bundle);
    }

    public List<FhirPatient> searchPatientsBySurname(String surname) throws FhirServerException
    {
        Bundle bundle = get(PATIENT_ENDPOINT, Bundle.class, ImmutableMap.of(FAMILY_PARAMETER, surname));
        return getPatients(bundle);
    }

    public List<FhirPatient> searchPatientsByGenderAndSurname(String gender, String surname) throws FhirServerException
    {
        Bundle bundle = get(PATIENT_ENDPOINT, Bundle.class, ImmutableMap.of(GENDER_PARAMETER, gender, FAMILY_PARAMETER, surname));
        return getPatients(bundle);
    }

    private List<FhirPatient> getPatients(Bundle bundle)
    {
        List<FhirPatient> result = new ArrayList<>();
        for (BundleEntry bundleEntry: bundle.getEntry()){
            result.add(bundleEntry.getResource());
        }
        return result;
    }

    private <T> T get(String endpoint, Class<T> type, Map<String, Object> parameters) throws FhirServerException
    {
        try {
            HttpRequest request = Unirest.get(server + endpoint)
                .header(ACCEPT_HEADER, ACCEPT_JSON)
                .queryString(parameters);
            HttpResponse<String> response = request.asString();

            if (response.getStatus() != 200){
                throw new FhirServerException(response.getStatus());
            }
            return serializer.deserialize(response.getBody(), type);
        }
        catch (UnirestException exception){
            throw new FhirServerException(exception);
        }
    }
}
