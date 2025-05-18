package todo.ui.services;

import java.util.ArrayList;
import java.util.List;
import todo.models.BenutzerModel;


public class BenutzerService {
      private final List<BenutzerModel> users = new ArrayList<>();

        public List<BenutzerModel> getAllUsers() {
            return users;
        }

        public void addUser(BenutzerModel user) {
            users.add(user);
        }

        public void deleteUser(BenutzerModel user) {
            users.remove(user);
        }
    }