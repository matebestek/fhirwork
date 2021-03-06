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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.narrative.DefaultThymeleafNarrativeGenerator;
import ca.uhn.fhir.narrative.INarrativeGenerator;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.CorsInterceptor;
import ca.uhn.fhir.rest.server.interceptor.ResponseHighlighterInterceptor;
import org.springframework.web.cors.CorsConfiguration;
import org.ucl.fhirwork.ApplicationService;

import javax.servlet.annotation.WebServlet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Instances of this class configure the system as a FHIR server, and specify
 * the two entry points into the system, {@link PatientResourceProvider} and
 * {@link ObservationResourceProvider}.
 *
 * @author Blair Butterworth
 * @author Didac Magrina
 * @author Koon Wei Teo
 * @author Evanthia Tingiri
 * @author Shruti Sinha
 * @author Yuan wei
 */
@WebServlet(urlPatterns= {"/fhir/*"}, displayName="FHIR Server")
public class FhirServer extends RestfulServer
{
    private ApplicationService applicationService;

    public FhirServer()
    {
        this(ApplicationService.instance());
    }

    public FhirServer(ApplicationService applicationService)
    {
        super(FhirContext.forDstu2());
        this.applicationService = applicationService;
    }

    @Override
    public void initialize()
    {
        setResourceProviders();
        enableDescriptiveErrors();
        enableCrossOriginScripting();
        enableOutputSyntaxHighlighting();
    }

    private void setResourceProviders()
    {
        List<IResourceProvider> providers = new ArrayList<IResourceProvider>();
        providers.add(applicationService.get(PatientResourceProvider.class));
        providers.add(applicationService.get(ObservationResourceProvider.class));
        providers.add(applicationService.get(FamilyHistoryResourceProvider.class));
        setResourceProviders(providers);
    }

    private void enableDescriptiveErrors()
    {
        INarrativeGenerator narrativeGen = new DefaultThymeleafNarrativeGenerator();
        getFhirContext().setNarrativeGenerator(narrativeGen);
    }

    private void enableOutputSyntaxHighlighting()
    {
        registerInterceptor(new ResponseHighlighterInterceptor());
        setDefaultPrettyPrint(true);
    }

    private void enableCrossOriginScripting()
    {
        CorsConfiguration config = new CorsConfiguration();
        CorsInterceptor corsInterceptor = new CorsInterceptor(config);
        config.addAllowedHeader("Accept");
        config.addAllowedHeader("Content-Type");
        config.addAllowedOrigin("*");
        config.addExposedHeader("Location");
        config.addExposedHeader("Content-Location");
        config.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS"));
        registerInterceptor(corsInterceptor);
    }
}
