//*************************************************//
//          INTHER LOGISTICS ENGINEERING           //
//*************************************************//

package com.inthergroup.webpoc.framework.helper;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gvandenbekerom
 * @since 13-Nov-18
 *
 * This is a generated class that is used to receive FilterStructure json string from the front-end
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "comparisonType",
        "filters"
})
public class FilterStructure {

    @JsonProperty("comparisonType")
    private String comparisonType;
    @JsonProperty("filters")
    private List<Filter> filters = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("comparisonType")
    public String getComparisonType() {
        return comparisonType;
    }

    @JsonProperty("comparisonType")
    public void setComparisonType(String comparisonType) {
        this.comparisonType = comparisonType;
    }

    @JsonProperty("filters")
    public List<Filter> getFilters() {
        return filters;
    }

    @JsonProperty("filters")
    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}

