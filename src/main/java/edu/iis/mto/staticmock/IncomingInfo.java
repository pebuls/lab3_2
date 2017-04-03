package edu.iis.mto.staticmock;

public class IncomingInfo {
	
	private final String content;
	private final SubscriptionType subscriptionType;

	public IncomingInfo(String content, SubscriptionType subscriptionType) {
		this.content = content;
		this.subscriptionType = subscriptionType;
	}
	public boolean requiresSubsciption() {
		return subscriptionType != SubscriptionType.NONE;
	}

	public String getContent() {
		return content;
	}

	public SubscriptionType getSubscriptionType() {
		return subscriptionType;
	}

}
