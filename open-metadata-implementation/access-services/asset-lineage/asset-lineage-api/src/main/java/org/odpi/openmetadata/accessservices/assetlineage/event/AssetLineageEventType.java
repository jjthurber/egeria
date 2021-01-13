/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.event;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Arrays;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AssetLineageEventType describes the different types of events can be produced by the Asset Lineage OMAS.
 * Events are limited to assets that are in the zones listed in the supportedZones property
 * passed to the Asset Lineage OMAS at start up (a null value here means all zones).
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public enum AssetLineageEventType implements Serializable {

    PROCESS_CONTEXT_EVENT(0, "ProcessContextEvent", "Has the full context for a Process"),
    GLOSSARY_TERM_CONTEXT_EVENT(1, "GlossaryTermContextEvent", "Has the full context for a Glossary Term including the context of schema elements"),
    CLASSIFICATION_CONTEXT_EVENT(2, "ClassificationContextEvent", "Has the full context for a classified element"),
    UPDATE_ENTITY_EVENT(3, "UpdateEvent", "Has the entity that is being updated"),
    DELETE_ENTITY_EVENT(4, "DeleteEvent", "Has the entity to be deleted"),
    NEW_RELATIONSHIP_EVENT(5, "NewRelationship", "Has the relationship that is being created"),
    UPDATE_RELATIONSHIP_EVENT(6, "UpdateRelationship", "Has the relationship that is being updated"),
    DELETE_RELATIONSHIP_EVENT(7, "DeleteRelationship", "Has the relationship to be deleted"),
    DECLASSIFIED_ENTITY_EVENT(8, "DeclassifiedEntityEvent", "All relevant lineage classifications for this entity have been removed"),
    RECLASSIFIED_ENTITY_EVENT(9, "ReclassifiedEntityEvent", "Has the full context for a classified element"),
    SEMANTIC_ASSIGNMENTS_EVENT(10, "SemanticAssignmentsEvent", "Has the semantic assignments for a glossary term"),
    TERM_CATEGORIZATIONS_EVENT(11, "TermCategorizationsEvent", "Has the term categorizations for a glossary term"),
    TERM_ANCHOR_EVENT(12, "TermAnchorEvent", "Has the term anchor for a glossary term"),
    GLOSSARY_CATEGORIES_EVENT(13, "GlossaryCategoriesEvent", "Has the categorizations for an anchor"),
    COLUMN_CONTEXT_EVENT(14, "ColumnContextEvent", "Has the context for a column"),
    ASSET_CONTEXT_EVENT(15, "AssetContextEvent", "Has the context for an asset"),
    LINEAGE_MAPPINGS_EVENT(16, "LineageMappingsEvent", "Has the linege mappings for a column"),
    UNKNOWN_ASSET_LINEAGE_EVENT(100, "UnknownAssetLineageEvent", "An AssetLineage OMAS event that is not recognized by the local handlers.");

    private static final long serialVersionUID = 1L;

    private int eventTypeCode;
    private String eventTypeName;
    private String eventTypeDescription;

    AssetLineageEventType(int eventTypeCode, String eventTypeName, String eventTypeDescription) {
        this.eventTypeCode = eventTypeCode;
        this.eventTypeName = eventTypeName;
        this.eventTypeDescription = eventTypeDescription;
    }

    /**
     * Gets event type code.
     *
     * @return the event type code
     */
    public int getEventTypeCode() {
        return eventTypeCode;
    }

    /**
     * Gets event type name.
     *
     * @return the event type name
     */
    public String getEventTypeName() {
        return eventTypeName;
    }

    /**
     * Gets event type description.
     *
     * @return the event type description
     */
    public String getEventTypeDescription() {
        return eventTypeDescription;
    }

    public static AssetLineageEventType getByEventTypeName(String eventTypeName) {

        return Arrays.stream(AssetLineageEventType.values()).filter(v -> v.eventTypeName.equals(eventTypeName)).findAny().orElse(null);
    }
}
