package org.lalelu.brivel.brivelplus.requests;

import org.lalelu.brivel.brivelplus.container.AddressContainer;
import org.lalelu.brivel.brivelplus.selectors.FieldSelector;

public class AddressRequest extends Request {

    public AddressRequest() {
        super(AddressContainer.class);

        addSelector(new FieldSelector("id", "poi_address", Long.class));
        addSelector(new FieldSelector("country", "poi_address", String.class));
        addSelector(new FieldSelector("name", "poi_address", String.class));
        addSelector(new FieldSelector("title", "poi_address", String.class));
    }
}
