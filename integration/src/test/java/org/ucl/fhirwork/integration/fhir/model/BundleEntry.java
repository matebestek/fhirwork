/*
 * FHIRWork (c)
 *
 * This work is licensed under the Creative Commons Attribution 4.0
 * International License. To view a copy of this license, visit
 *
 *      http://creativecommons.org/licenses/by/4.0/
 */

package org.ucl.fhirwork.integration.fhir.model;

public class BundleEntry
{
    private String fullUrl;
    private FhirPatient resource;

    public BundleEntry(String fullUrl, FhirPatient resource) {
        this.fullUrl = fullUrl;
        this.resource = resource;
    }

    public String getFullUrl() {
        return fullUrl;
    }

    public FhirPatient getResource() {
        return resource;
    }
}
