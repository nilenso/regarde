(ns regarde.models.errors)

(defn generate-error-check [conditions]
  (fn [record]
    (let [errors (reduce (fn [errors [f msg]]
                           (if (f record)
                             (cons msg errors)
                             errors))
                         []
                         conditions)]
      (if (empty? errors)
        nil
        errors))))
