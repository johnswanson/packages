(set-env!
  :resource-paths #{"resources"}
  :dependencies '[[adzerk/bootlaces   "0.1.10" :scope "test"]
                  [cljsjs/boot-cljsjs "0.5.0"  :scope "test"]
                  [cljsjs/react       "0.13.3-1"]])

(require '[adzerk.bootlaces :refer :all]
         '[cljsjs.boot-cljsjs.packaging :refer :all])

(def +version+ "2.0.2-0")
(bootlaces! +version+)

(task-options!
 pom  {:project     'cljsjs/react-dnd
       :version     +version+
       :description "Drag and Drop for React"
       :url         "https://github.com/gaearon/react-dnd"
       :scm         {:url "https://github.com/cljsjs/packages"}
       :license     {"MIT" "http://opensource.org/licenses/MIT"}})

(deftask download-react-dnd []
  (download :url      "https://github.com/gaearon/react-dnd/archive/v2.0.2.zip"
            :checksum "B149119AC33588BEF74D6AE6E7F6B47B"
            :unzip    true))

(deftask package []
  (comp
    (download-react-dnd)
    (sift :move {#"^react-.*/dist/ReactDnD.min.js"
                 "cljsjs/react-dnd/development/react-dnd.inc.js"})
    (minify :in "cljsjs/react-dnd/development/react-dnd.inc.js"
            :out "cljsjs/react-dnd/production/react-dnd.min.inc.js")
    (sift :include #{#"^cljsjs"})
    (deps-cljs :name "cljsjs.react-dnd"
               :requires ["cljsjs.react"])))
