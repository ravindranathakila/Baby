package com.google.contacts.api.impl;

import com.google.contacts.api.impl.Client;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: 12/18/10
 * Time: 9:16 PM
 *
 * @author Ravindranath Akila
 */
public interface ClientFactory {
    public Client getInstance(final String jsonEndpoint);
}
