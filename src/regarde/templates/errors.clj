(ns regarde.templates.errors)

(defn render [errors]
  (str "<ul>"
       (apply str (map #(str "<li>" % "</li>") errors))
       "</ul>"))
