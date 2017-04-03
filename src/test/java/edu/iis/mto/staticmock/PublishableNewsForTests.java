package edu.iis.mto.staticmock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by pebuls on 03.04.17.
 */

public class PublishableNewsForTests extends PublishableNews {

    public final List<String> publicNews = new ArrayList<>();
    public final HashMap<String, SubscriptionType> subscribedNews = new HashMap<>();

    @Override
    public void addPublicInfo(String content) {
        super.addPublicInfo(content);
        publicNews.add(content);
    }

    @Override
    public void addForSubscription(String content, SubscriptionType subscriptionType) {
        super.addForSubscription(content, subscriptionType);
        subscribedNews.put(content, subscriptionType);
    }
}
