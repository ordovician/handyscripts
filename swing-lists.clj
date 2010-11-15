; A program for selecting a subset of symbols used for knitting. We basically
; have to lists. On the left side a list of all knitting symbols we can chose
; from and on the right side a list of a subset of knitting symbols we have
; selected
;
; An important feature is the ability to move elements in each list up and
; down. This allows us to put frequently used elements at the top for the first
; list and determine the order the knitting icons are shown the patterns
; receipt 
(import '(javax.swing JFrame JPanel JButton JOptionPane JList DefaultListModel BoxLayout Box BorderFactory JScrollPane))
(import '(java.awt.event ActionListener))
(import '(java.awt FlowLayout Dimension))

(def knitting-symbols-list ["erik" "lani" "john" "christian" "Rob" "Dilbert" "Hans" "Bodie" "Cody" "Ralph" "Guru" "Kick-Ass" "Red Mist" "And so on"])

; Helper macro for defining java action listeners
(defmacro on-action [component event & body]
  `(. ~component addActionListener
      (proxy [java.awt.event.ActionListener] []
        (actionPerformed [~event] ~@body))))

; Moves an item in the list 'item-list' 'offset' number of places up
(defn move-in-list [item-list offset]
  (let [model (. item-list getModel)
        index (. item-list getSelectedIndex)
        item  (. model getElementAt index)
        item-above (. model getElementAt (+ index offset))]
    (. model setElementAt item-above index)
    (. model setElementAt item (+ index offset))
    (. item-list setSelectedIndex (+ index offset)))) 

(defn create-border [m]
  (BorderFactory/createEmptyBorder m m m m))

(def btn-dim (Dimension. 100 25))
(defn set-size [comp dim] 
  (. comp setMinimumSize dim)
  (. comp setMaximumSize dim))


; Creats a JList in a panel containg an up and down button. A vector pair with
; [panel JList] is returned
(defn create-list [model]
  (let [item-list (JList. model)
        scroll-pane (JScrollPane. item-list)
        top  (doto (JButton. "Top")
               (set-size btn-dim) 
               (on-action event
                          (move-in-list item-list 
                                        (- (. item-list getSelectedIndex)))))

        up   (doto (JButton. "Up")
               (set-size btn-dim)
               (on-action event
                          (move-in-list item-list -1)))
        down (doto (JButton. "Down")
               (set-size btn-dim)
               (on-action event
                          (move-in-list item-list 1)))
;        button-grp (doto (Box/createVerticalBox)
;                (.add up)
;                (.add down))
        panel (doto (Box/createVerticalBox)
                (.add top)
                (.add up)
                (.add down)
;                (.add button-grp)
                (.add scroll-pane)
                (.setBorder (create-border 5)))]
    [panel item-list]))

; Moves selected element from list 'src-list' to 'dst-list' Both lists are of
; type JList
(defn move-element [src-list dst-list]
  (let [src-model (. src-list getModel)
        dst-model (. dst-list getModel)
				src-index (. src-list getSelectedIndex)
        dst-index (. dst-list getSelectedIndex)]
    (. dst-model addElement 
       (. src-model getElementAt src-index))
    (. dst-list setSelectedValue 
       (. src-model getElementAt src-index) true)
    (. src-model remove src-index)
;   (. src-list setSelectedIndex src-index)
    (. dst-list setSelectedIndex (. dst-model getSize))))

; Creates a panel with a add and remove button, used to move elements back and
; forth between the two lists 'src-list' and 'dst-list'
(defn create-assign-pane [src-list dst-list]
  (let [add-btn     (doto (JButton. "Add >")
                      (set-size (Dimension. 120 25))
                      (on-action event
                                 (move-element src-list dst-list)))
        remove-btn  (doto (JButton. "< Remove")
                      (set-size (Dimension. 120 25))
                      (on-action event
                                 (move-element dst-list src-list)))

        panel (doto (Box/createVerticalBox)
                (.add add-btn)
                (.add remove-btn)
                (.setBorder (create-border 5)))]
    panel))

; Creates a default list model base on the strings in vector 'xs'
(defn create-list-model [xs]
  (let [model (DefaultListModel.)]
    (doseq [x xs]
      (. model addElement x))
    model))
        

(defn knitting-app []
  (let [all-list-model (create-list-model knitting-symbols-list) 
        selection-list-model (DefaultListModel.)
        all-list-pair       (create-list all-list-model)
        selection-list-pair (create-list selection-list-model)
        all-list       (last all-list-pair)
        selection-list  (last selection-list-pair)
        all-pane       (first all-list-pair)
        selection-pane (first selection-list-pair)
        assign-pane    (create-assign-pane all-list selection-list)

        panel (doto (JPanel. (FlowLayout.))
                (.add all-pane)
                (.add assign-pane)
                (.add selection-pane))]

    (doto (JFrame. "Knitting symbols assignment")
      (.setContentPane panel)
      (.pack)
      (.setVisible true)
      (.setDefaultCloseOperation JFrame/EXIT_ON_CLOSE))))
(knitting-app)

                  
