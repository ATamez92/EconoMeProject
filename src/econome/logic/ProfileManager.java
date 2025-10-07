package econome.logic;

import econome.model.Profile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the creation, loading, and deletion of user profiles.
 * <p>
 * Profiles are serialized to a local file (profiles.dat) for persistence.
 * This allows the splash screen to list existing profiles on startup.
 */
public class ProfileManager {
    private static final String FILE_NAME = "profiles.dat";

    private List<Profile> profiles;

    public ProfileManager() {
        profiles = loadProfiles();
    }

    /**
     * @return a list of all stored profiles.
     */
    public List<Profile> getProfiles() {
        return profiles;
    }

    /**
     * Adds a new profile and saves to disk.
     *
     * @param profile The new profile to add.
     */
    public void addProfile(Profile profile) {
        profiles.add(profile);
        saveProfiles();
    }

    /**
     * Deletes a profile and saves the updated list.
     *
     * @param profile The profile to remove.
     */
    public void deleteProfile(Profile profile) {
        profiles.remove(profile);
        saveProfiles();
    }

    /**
     * Loads the list of profiles from the local file.
     *
     * @return A list of stored profiles, or an empty list if none exist.
     */
    @SuppressWarnings("unchecked")
    private List<Profile> loadProfiles() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Profile>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("⚠️ Failed to load profiles: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Saves the current list of profiles to disk.
     */
    private void saveProfiles() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(profiles);
        } catch (IOException e) {
            System.err.println("⚠️ Failed to save profiles: " + e.getMessage());
        }
    }

    /**
     * Finds a profile by name.
     *
     * @param name The name of the profile.
     * @return The matching Profile or null if not found.
     */
    public Profile findProfileByName(String name) {
        for (Profile p : profiles) {
            if (p.getName().equalsIgnoreCase(name)) {
                return p;
            }
        }
        return null;
    }
}
