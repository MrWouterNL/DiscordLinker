package nl.minetopiasdb.discordbot.utils.link;

import java.util.HashMap;
import java.util.UUID;

public class LinkUtils {

	private static LinkUtils link = new LinkUtils();
	
	//LinkID, discord long id
	private static HashMap<UUID, Long> pendingLinks = new HashMap<>();
	
	
	public static LinkUtils getInstance() {
		return link;
	}
	
	public UUID registerLink(long userId) {
		UUID id = getRandomID();
		pendingLinks.put(id, userId);
		return id;
	}
	
	public void removeLink(UUID u) {
		pendingLinks.remove(u);
	}
	
	public void removeLink(long userid) {
		for (UUID uid: pendingLinks.keySet()) {
			if (getUser(uid) == userid) {
				removeLink(uid);
			}
		}
	}
	
	public long getUser(UUID u) {
		return pendingLinks.get(u);
	}
	
	public boolean isValidLink(UUID u) {
		return pendingLinks.containsKey(u);
	}
	
	
	public boolean isValidLink(long userId) {
		return pendingLinks.containsValue(userId);
	}
		
	private UUID getRandomID() {
		UUID random = UUID.randomUUID();
		
		//This change is very, very small.
		if (pendingLinks.containsKey(random)) {
			return getRandomID();
		}
		return random;
	}

}
