/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.repositoryhandler;

import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * RepositoryRelatedEntitiesIterator is an iterator class for iteratively retrieving relationships for an starting entity (possibly restricting
 * the type of relationships returned) and returning the entity at the other end.  It is used where the caller needs to filter the results coming
 * from the repository and may need to make more than one call to the repository in order to accumulate the number of requested results.
 *
 * Note this class is intended for a single request's use - it is not thread-safe.
 */
public class RepositoryRelatedEntitiesIterator extends RepositoryIteratorForEntities
{
    private String             startingEntityGUID;
    private String             startingEntityTypeName;
    private String             relationshipTypeGUID;
    private String             relationshipTypeName;
    private int                selectionEnd = 0;
    private static final Logger log = LoggerFactory.getLogger(RepositoryRelatedEntitiesIterator.class);


    /**
     * Constructor takes the parameters used to call the repository handler.
     *
     * @param repositoryHandler interface to the open metadata repositories.
     * @param userId  user making the request
     * @param startingEntityGUID  starting entity's GUID
     * @param startingEntityTypeName  starting entity's type name
     * @param relationshipTypeGUID  identifier for the relationship to follow
     * @param relationshipTypeName  type name for the relationship to follow
     * @param sequencingPropertyName name of property used to sequence the results - null means no sequencing
     * @param startingFrom initial position in the stored list.
     * @param pageSize maximum number of definitions to return on this call.
     * @param methodName  name of calling method
     */
    public RepositoryRelatedEntitiesIterator(RepositoryHandler repositoryHandler,
                                             String            userId,
                                             String            startingEntityGUID,
                                             String            startingEntityTypeName,
                                             String            relationshipTypeGUID,
                                             String            relationshipTypeName,
                                             String            sequencingPropertyName,
                                             int               startingFrom,
                                             int               pageSize,
                                             String            methodName)
    {
        super(repositoryHandler, userId, null, null, sequencingPropertyName, startingFrom, pageSize, methodName);

        this.startingEntityGUID     = startingEntityGUID;
        this.startingEntityTypeName = startingEntityTypeName;
        this.relationshipTypeGUID   = relationshipTypeGUID;
        this.relationshipTypeName   = relationshipTypeName;
    }
    /**
     * Constructor takes the parameters used to call the repository handler.
     *
     * @param repositoryHandler interface to the open metadata repositories.
     * @param userId  user making the request
     * @param startingEntityGUID  starting entity's GUID
     * @param startingEntityTypeName  starting entity's type name
     * @param relationshipTypeGUID  identifier for the relationship to follow
     * @param relationshipTypeName  type name for the relationship to follow
     * @param sequencingPropertyName name of property used to sequence the results - null means no sequencing
     * @param startingFrom initial position in the stored list.
     * @param pageSize maximum number of definitions to return on this call.
     * @param selectionEnd 0 means either end, 1 means only take from end 1, 2 means only take from end 2
     * @param methodName  name of calling method
     */
    public RepositoryRelatedEntitiesIterator(RepositoryHandler repositoryHandler,
                                             String            userId,
                                             String            startingEntityGUID,
                                             String            startingEntityTypeName,
                                             String            relationshipTypeGUID,
                                             String            relationshipTypeName,
                                             String            sequencingPropertyName,
                                             int               startingFrom,
                                             int               pageSize,
                                             int               selectionEnd,
                                             String            methodName)
    {
        this(repositoryHandler, userId, startingEntityGUID, startingEntityTypeName, relationshipTypeGUID, relationshipTypeName, sequencingPropertyName, startingFrom, pageSize, methodName);
        if (log.isDebugEnabled())
        {
            log.debug("RepositoryRelatedEntitiesIterator :startingFrom=" + startingFrom + ",startingEntityGUID=" + startingEntityGUID);
        }
        this.selectionEnd =  selectionEnd;
    }


    /**
     * Determine if there is more to receive.  It will populate the iterator's cache with more content.
     *
     * @return boolean flag
     * @throws UserNotAuthorizedException the repository is not allowing the user to access the metadata
     * @throws PropertyServerException there is a problem in the repository
     */
    @Override
    public boolean  moreToReceive() throws UserNotAuthorizedException,
                                           PropertyServerException
    {
        if ((entitiesCache == null) || (entitiesCache.isEmpty()))
        {
            if (selectionEnd == 0)
            {
                entitiesCache = repositoryHandler.getEntitiesForRelationshipType(userId,
                                                                                 startingEntityGUID,
                                                                                 startingEntityTypeName,
                                                                                 relationshipTypeGUID,
                                                                                 relationshipTypeName,
                                                                                 startingFrom,
                                                                                 pageSize,
                                                                                 methodName);
            } else
            {
                boolean anchorAtEnd1 = false;
                if (selectionEnd == 2) {
                    anchorAtEnd1 = true;
                } else {
                    anchorAtEnd1 = false;
                }
                entitiesCache = repositoryHandler.getEntitiesForRelationshipEnd(userId,
                                                                                startingEntityGUID,
                                                                                startingEntityTypeName,
                                                                                anchorAtEnd1,
                                                                                relationshipTypeGUID,
                                                                                relationshipTypeName,
                                                                                startingFrom,
                                                                                pageSize,
                                                                                methodName);
            }

            if (entitiesCache != null)
            {
                if (log.isDebugEnabled())
                {
                    log.debug("RepositoryRelatedEntitiesIterator : moreToReceive() entitiesCache not null");
                    for (EntityDetail entityDetail : entitiesCache)
                    {
                        String displayName = "";
                        String qualifiedName = "";
                        if (entityDetail.getProperties() != null && entityDetail.getProperties().getInstanceProperties() != null)
                         {
                             if ( entityDetail.getProperties().getInstanceProperties().get("displayName") !=null) {
                                 displayName = entityDetail.getProperties().getInstanceProperties().get("displayName").toString();
                             }
                             if ( entityDetail.getProperties().getInstanceProperties().get("qualifiedName") !=null) {
                                 qualifiedName = entityDetail.getProperties().getInstanceProperties().get("qualifiedName").toString();
                             }
                        }
                        log.debug("Cached entity " + entityDetail.getGUID() + ",displayName=" + displayName + ",qualifiedName=" +qualifiedName );
                    }
                }
                startingFrom = startingFrom + entitiesCache.size();
                if (log.isDebugEnabled()) {
                    log.debug("StartingFrom=" + startingFrom);
                }
            }
        }

        return entitiesCache != null;
    }
}
