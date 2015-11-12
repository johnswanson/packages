(set-env!
  :resource-paths #{"resources"}
  :dependencies '[[adzerk/bootlaces   "0.1.10" :scope "test"]
                  [cljsjs/boot-cljsjs "0.5.0"  :scope "test"]
                  [cljsjs/react       "0.13.3-1"]])

(require '[adzerk.bootlaces :refer :all]
         '[cljsjs.boot-cljsjs.packaging :refer :all])

(def +version+ "2.0.0-0")
(bootlaces! +version+)

(task-options!
 pom  {:project     'cljsjs/react-dnd-html5-backend
       :version     +version+
       :description "The officially supported HTML5 backend for React DnD."
       :url         "https://github.com/gaearon/react-dnd-html5-backend"
       :scm         {:url "https://github.com/cljsjs/packages"}
       :license     {"MIT" "http://opensource.org/licenses/MIT"}})

(deftask download-react-dnd []
  (download :url      "https://github.com/gaearon/react-dnd-html5-backend/archive/v2.0.0.zip"
            :checksum "9A31E6508A2008159449B7A1BA0FC751"
            :unzip    true))

(deftask package []
  (comp
    (download-react-dnd)
    (sift :move {#"^react-.*/dist/ReactDnDHTML5Backend.min.js"
                 "cljsjs/react-dnd/development/react-dnd-html5-backend.inc.js"})
    (minify :in "cljsjs/react-dnd/development/react-dnd-html5-backend.inc.js"
            :out "cljsjs/react-dnd/production/react-dnd-html5-backend.min.inc.js")
    (sift :include #{#"^cljsjs"})
    (deps-cljs :name "cljsjs.react-dnd-html5-backend"
               :requires ["cljsjs.react"])))
