package econome.logic;

import econome.model.Profile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles the creation, retrieval, deletion, and persistence of user profiles
 * in the EconoMe application.
 * <p>
 * Profiles are serialized to a local file ({@code profiles.dat}) to preserve
 * user data between sessions. This manager ensures that any changes to
 * profiles—such as additions, deletions, or updates—are automatically saved
 * to disk.
 * </p>
 *
 * <h3>Responsibilities:</h3>
 * <ul>
 *   <li>Load saved profiles at application startup.</li>
 *   <li>Add, delete, or update profiles and persist those changes to disk.</li>
 *   <li>Provide controlled access to the list of stored profiles.</li>
 * </ul>
 */
public class ProfileManager {

    // --- Constants ------------------------------------------------------------

    /** The file name used to store serialized profile data locally. */
    private static final String PROFILE_STORAGE_FILE = "profiles.dat";


    // --- Fields ---------------------------------------------------------------

    /** In-memory list containing all stored user profiles. */
    private final List<Profile> profiles;


    // --- Constructors ---------------------------------------------------------

    /**
     * Constructs a new {@code ProfileManager} and automatically loads
     * any saved profiles from disk into memory.
     */
    public ProfileManager() {
        this.profiles = loadProfiles();
    } // End of constructor ProfileManager


    // --- Public Methods -------------------------------------------------------

    /**
     * Retrieves all profiles currently stored in memory.
     *
     * @return a mutable {@link List} of {@link Profile} objects
     */
    public List<Profile> getProfiles() {
        return profiles;
    } // End of method getProfiles


    /**
     * Adds a new profile to the in-memory list and immediately persists
     * all profiles to disk.
     *
     * @param newProfile the new {@link Profile} to add
     */
    public void addProfile(Profile newProfile) {
        profiles.add(newProfile);
        saveProfiles();
    } // End of method addProfile


    /**
     * Deletes a profile from the in-memory list and updates the
     * serialized data file on disk.
     *
     * @param profileToDelete the {@link Profile} to remove
     */
    public void deleteProfile(Profile profileToDelete) {
        profiles.remove(profileToDelete);
        saveProfiles();
    } // End of method deleteProfile


    /**
     * Searches the current list for a profile with a matching name.
     *
     * @param name the name of the profile to find (case-insensitive)
     * @return the matching {@link Profile}, or {@code null} if not found
     */
    public Profile findProfileByName(String name) {
        for (Profile profileItem : profiles) {
            if (profileItem.getName().equalsIgnoreCase(name)) {
                return profileItem;
            }
        } // End of loop
        return null;
    } // End of method findProfileByName


    // --- Private Persistence Methods -----------------------------------------

    /**
     * Loads all profiles from the serialized data file.
     * <p>
     * If no file exists or the file cannot be read, this method returns an
     * empty {@link ArrayList} to ensure the program remains stable.
     * </p>
     *
     * @return a list of loaded {@link Profile} objects, or an empty list
     */
    @SuppressWarnings("unchecked")
    private List<Profile> loadProfiles() {
        File file = new File(PROFILE_STORAGE_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Profile>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("⚠️ [ProfileManager] Failed to load profiles: " + e.getMessage());
            return new ArrayList<>();
        }
    } // End of method loadProfiles


    /**
     * Serializes and saves the current in-memory list of profiles to disk.
     * <p>
     * Should be invoked after any operation that modifies the list of profiles.
     * </p>
     */
    private void saveProfiles() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(PROFILE_STORAGE_FILE))) {
            outputStream.writeObject(profiles);
        } catch (IOException e) {
            System.err.println("⚠️ [ProfileManager] Failed to save profiles: " + e.getMessage());
        }
    } // End of method saveProfiles

} // End of class ProfileManager