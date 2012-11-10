(ns jukebox-compiler.core
  (:use [overtone.live]))

(definst beep [note 60]
  (let
      [src (sin-osc (midicps note))
       env (env-gen (perc 0.01 0.9) :action FREE)]
    (* src env)))

(definst bell [frequency 300 duration 10
               h0 1 h1 0.6 h2 0.4 h3 0.25 h4 0.2 h5 0.15]
  (let [harmonics   [ 1  2  3  4.2  5.4  6.8]
        proportions [h0 h1 h2 h3 h4 h5]
        proportional-partial
         (fn [harmonic proportion]
           (let [envelope (env-gen (perc 0.01 (* proportion duration)))
                 overtone (* harmonic frequency)]
             (* 1/2 proportion envelope (sin-osc overtone))))
        partials (map proportional-partial harmonics proportions)
        whole (mix partials)]
    (detect-silence whole :action FREE)
    whole))

(bell)
