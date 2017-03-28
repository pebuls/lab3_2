package lab3_2;

import java.util.ArrayList;
import java.util.List;

import edu.iis.mto.staticmock.PublishableNews;
import edu.iis.mto.staticmock.SubsciptionType;

class PublishableNewsContentChecker extends PublishableNews {
		private final List<String> publicContent = new ArrayList<>();
		private final List<String> subscribentContent = new ArrayList<>();
		
		@Override
		public void addPublicInfo(String content) {
			super.addPublicInfo(content);
			this.publicContent.add(content);
		}
		
		@Override
		public void addForSubscription(String content, SubsciptionType subscriptionType) {
			super.addForSubscription(content, subscriptionType);
			this.subscribentContent.add(content);
		}
		
		public List<String> getPublicContent() {
			return this.publicContent;
		}
		
		public List<String> getSubscribentContent() {
			return this.subscribentContent;
		}
	}