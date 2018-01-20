/*
 * FHIRWork (c)
 *
 * This work is licensed under the Creative Commons Attribution 4.0
 * International License. To view a copy of this license, visit
 *
 *      http://creativecommons.org/licenses/by/4.0/
 */

package org.ucl.fhirwork.integration.serialization;

import com.google.gson.Gson;

public class JsonSerializer
{
    public <T> T deserialize(String content, Class<T> type)
    {
        Gson gson = new Gson();
        return gson.fromJson(content, type);
    }
}
