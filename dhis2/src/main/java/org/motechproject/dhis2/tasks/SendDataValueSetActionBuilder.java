package org.motechproject.dhis2.tasks;

import org.motechproject.dhis2.domain.DataElement;
import org.motechproject.dhis2.domain.DataSet;
import org.motechproject.dhis2.event.EventParams;
import org.motechproject.dhis2.event.EventSubjects;
import org.motechproject.tasks.contract.ActionEventRequest;
import org.motechproject.tasks.contract.builder.ActionEventRequestBuilder;
import org.motechproject.tasks.contract.ActionParameterRequest;
import org.motechproject.tasks.contract.builder.ActionParameterRequestBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Builds task action requests for each data set.
 */
public class SendDataValueSetActionBuilder {

    /**
     * Creates a list of action event requests based on the given {@code dataSets}.
     *
     * @param dataSets  the list of data sets
     * @return the list of action event requests
     */
    public List<ActionEventRequest> addSendDataValueSetActions(List<DataSet> dataSets) {
        List<ActionEventRequest> requests = new ArrayList<>();

        for (DataSet dataSet : dataSets) {
            requests.add(addSendDataValueSetAction(dataSet));
        }

        return requests;
    }

    private ActionEventRequest addSendDataValueSetAction(DataSet dataSet) {
        int order = 0;
        ActionParameterRequestBuilder builder = new ActionParameterRequestBuilder();
        SortedSet<ActionParameterRequest> actionParameterRequests = new TreeSet<>();

        builder.setDisplayName(DisplayNames.DATA_SET)
                .setKey(EventParams.DATA_SET)
                .setOrder(order++);
        actionParameterRequests.add(builder.createActionParameterRequest());

        builder = new ActionParameterRequestBuilder();
        builder.setDisplayName(DisplayNames.COMPLETE_DATE)
                .setKey(EventParams.COMPLETE_DATE)
                .setOrder(order++);
        actionParameterRequests.add(builder.createActionParameterRequest());

        builder = new ActionParameterRequestBuilder();
        builder.setDisplayName(DisplayNames.PERIOD)
                .setKey(EventParams.PERIOD)
                .setOrder(order++)
                .setRequired(true);
        actionParameterRequests.add(builder.createActionParameterRequest());

        builder = new ActionParameterRequestBuilder();
        builder.setDisplayName(DisplayNames.ORG_UNIT)
                .setKey(EventParams.LOCATION)
                .setOrder(order++)
                .setRequired(true);
        actionParameterRequests.add(builder.createActionParameterRequest());

        builder = new ActionParameterRequestBuilder();
        builder.setDisplayName(DisplayNames.CATEGORY_OPTION_COMBO)
                .setKey(EventParams.CATEGORY_OPTION_COMBO)
                .setOrder(order++);
        actionParameterRequests.add(builder.createActionParameterRequest());

        builder = new ActionParameterRequestBuilder();
        builder.setDisplayName(DisplayNames.COMMENT)
                .setKey(EventParams.COMMENT)
                .setOrder(order++);
        actionParameterRequests.add(builder.createActionParameterRequest());

        builder = new ActionParameterRequestBuilder();
        builder.setDisplayName(DisplayNames.ATTRIBUTE_OPTION_COMBO)
                .setKey(EventParams.ATTRIBUTE_OPTION_COMBO)
                .setOrder(order++);
        actionParameterRequests.add(builder.createActionParameterRequest());

        for (DataElement dataElement : dataSet.getDataElementList()) {
            builder = new ActionParameterRequestBuilder();
            builder.setDisplayName(dataElement.getName())
                    .setKey(dataElement.getUuid())
                    .setOrder(order++);
            actionParameterRequests.add(builder.createActionParameterRequest());
        }

        builder = new ActionParameterRequestBuilder();
        builder.setKey(EventParams.DATA_SET)
                .setDisplayName(DisplayNames.DATA_SET)
                .setHidden(true)
                .setValue(dataSet.getUuid())
                .setOrder(order);
        actionParameterRequests.add(builder.createActionParameterRequest());

        ActionEventRequestBuilder eventRequestBuilder = new ActionEventRequestBuilder();
        eventRequestBuilder.setActionParameters(actionParameterRequests)
                .setDisplayName(String.format("%s [%s]", DisplayNames.SEND_DATA_VALUE_SET, dataSet.getName()))
                .setSubject(EventSubjects.SEND_DATA_VALUE_SET)
                .setName(String.format("%s [%s]", DisplayNames.SEND_DATA_VALUE_SET, dataSet.getName()));

        return eventRequestBuilder.createActionEventRequest();
    }

}
