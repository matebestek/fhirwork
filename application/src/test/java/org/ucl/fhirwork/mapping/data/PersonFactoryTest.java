/*
 * FHIRWork (c) 2018 - Blair Butterworth, Abdul-Qadir Ali, Xialong Chen,
 * Chenghui Fan, Alperen Karaoglu, Jiaming Zhou
 *
 * This work is licensed under the MIT License. To view a copy of this
 * license, visit
 *
 *      https://opensource.org/licenses/MIT
 */

package org.ucl.fhirwork.mapping.data;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.parser.IParser;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.ucl.fhirwork.network.empi.data.Gender;
import org.ucl.fhirwork.network.empi.data.Identifier;
import org.ucl.fhirwork.network.empi.data.Person;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class PersonFactoryTest
{
    @Test
    public void fromPatientTest() throws IOException
    {
        GenderFactory genderFactory = new GenderFactory();
        IdentifierFactory identifierFactory = new IdentifierFactory();
        PersonFactory personFactory = new PersonFactory(genderFactory, identifierFactory);

        Patient patient = readPatient("fhir/PatientExample.json");
        Person person = personFactory.fromPatient(patient);

        Assert.assertEquals("1", person.getPersonId());

        Identifier[] identifiers = person.getPersonIdentifiers();
        Assert.assertEquals(2, identifiers.length);
        Assert.assertEquals("568749875445698798988873", identifiers[0].getIdentifier());
        Assert.assertEquals("OpenMRS", identifiers[0].getIdentifierDomain().getIdentifierDomainName());
        Assert.assertEquals("2b869d20-6ccc-11e7-a2fc-0242ac120003", identifiers[1].getIdentifier());
        Assert.assertEquals("OpenEMPI", identifiers[1].getIdentifierDomain().getIdentifierDomainName());

        Assert.assertEquals("Kathrin Mary", person.getGivenName());
        Assert.assertEquals("Williams", person.getFamilyName());

        Gender gender = person.getGender();
        Assert.assertNotNull(gender);
        Assert.assertEquals("Female", gender.getGenderName());
    }

    private Patient readPatient(String resource) throws IOException
    {
        IParser serializer = FhirContext.forDstu2().newJsonParser();
        String person = readResource(resource);
        return (Patient)serializer.parseResource(person);
    }

    private String readResource(String resource) throws IOException
    {
        URL templateUrl = Thread.currentThread().getContextClassLoader().getResource(resource);
        File templateFile = new File(templateUrl.getPath());
        return FileUtils.readFileToString(templateFile, StandardCharsets.UTF_8);
    }
}
