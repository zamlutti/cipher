(ns cipher.core-test
  (:require [cipher.core :as core]
            [midje.sweet :refer :all]
            [clojure.string :as str]))

(facts "takes a lowercase letter character and returns its position in the alphabet: a = 0, b = 1, etc."
  (tabular
    (core/to-int ?char) => ?position
    ?char ?position
    \a    0
    \b    1
    \c    2
    \d    3
    \e    4))

(facts "on to-char"
  (tabular
    (core/to-char ?int) => ?char
    ?int ?char
    0    \a
    1    \b
    2    \c
    3    \d
    4    \e))

(facts "on shift"
  (tabular
    (core/shift ?letter ?number) => ?letter-shifted
    ?letter ?number ?letter-shifted
    \a      3       \d
    \b      20      \v
    \z      3       \c
    \d      -3      \a
    \a      100     \w
    \a      22      \w))

(fact "map to-int"
  (mapv core/to-int [\a \b \c]) => [0 1 2]
  (mapv core/to-int "abc") => [0 1 2])

(fact "map shift"
  (mapv #(core/shift % 3) "batata") => [\e \d \w \d \w \d])

(fact "apply str"
  (apply str [\e \d \w \d \w \d]) => "edwdwd")

(facts "on encrypt"
  (tabular
    (core/encrypt ?word ?key) => ?result
    ?word    ?key ?result
    "batata" 3    "edwdwd"
    "apple"  20   "ujjfy"))

(facts "on decrypt"
  (tabular
    (core/decrypt ?word ?key) => ?result
    ?word    ?key ?result
    "edwdwd" 3    "batata"
    "ujjfy"  20   "apple"
    "tnrjzstrjjfssj" 5 "oimeunomeeanne"
    "vgprtwdeetglphdctduiwtuxghiegdvgpbbtghduiwtwpgkpgsbpgzdctrdbejitgpcsxcktcitsiwtuxghirdbexatgudgprdbejitgegdvgpbbxcvapcvjpvthwtedejapgxotsiwtxstpdubprwxctxcstetcstciegdvgpbbxcvapcvjpvthlwxrwatsidiwtstktadebtcidurdqdadctduiwtuxghiwxvwatktaegdvgpbbxcvapcvjpvthdlxcvidwtgprrdbeaxhwbtcihpcswtgcpkpagpczhwtlphhdbtixbthgtutggtsidphpbpoxcvvgprtiwtjhcpknpgatxvwqjgztraphhvjxstsbxhhxatsthigdntgjhhwdeetglphcpbtsudgwtghwtlphedhiwjbdjhanplpgstsiwtegthxstcixpabtspaduugttsdbqnegthxstciqpgprzdqpbp" 15 "gracehopperwasoneofthefirstprogrammersoftheharvardmarkonecomputerandinventedthefirstcompilerforacomputerprogramminglanguageshepopularizedtheideaofmachineindependentprogramminglanguageswhichledtothedevelopmentofcoboloneofthefirsthighlevelprogramminglanguagesowingtoheraccomplishmentsandhernavalrankshewassometimesreferredtoasamazinggracetheusnavyarleighburkeclassguidedmissiledestroyerusshopperwasnamedforhershewasposthumouslyawardedthepresidentialmedaloffreedombypresidentbarackobama"))

(fact "on get-letters"
  (core/get-letters "Hello, friends!") => "hellofriends"
  (core/get-letters "Oi, meu nome é Anne!") => "oimeunomeeanne")

(fact "on encrypt-letters"
  (core/encrypt-letters "Hello, friends!" 5) => "mjqqtkwnjsix"
  (core/encrypt-letters "Oi, meu nome é Anne!" 5) => "tnrjzstrjjfssj"
  (core/encrypt-letters "Grace Murray Hopper foi uma analista de sistemas da Marinha dos Estados Unidos e almirante. Criou a linguagem de programação Flow-Matic que serviu como base para a criação do COBOL. Foi também uma das primeiras programadoras do Harvard Mark One." 42) => "whqsuckhhqoxeffuhveykcqqdqbyijqtuiyijucqitqcqhydxqteiuijqteikdyteiuqbcyhqdjushyekqbydwkqwuctufhewhqcqsqevbemcqjysgkuiuhlyksecerqiufqhqqshyqsqeteserebveyjqcruckcqtqifhycuyhqifhewhqcqtehqitexqhlqhtcqhaedu")

(fact "on encrypt-text"
  (core/encrypt-text "Hello, friends!" 5) => "mjqqt, kwnjsix!"
  (core/encrypt-text (str "Grace Murray Hopper foi uma analista de sistemas"
                          " da Marinha dos Estados Unidos e almirante."
                          " Criou a linguagem de programação Flow-Matic que serviu"
                          " como base para a criação do COBOL."
                          "Foi também uma das primeiras programadoras do Harvard "
                          "Mark One.") 42) => "whqsu ckhhqo xeffuh vey kcq qdqbyijq tu iyijucqi tq cqhydxq tei uijqtei kdytei u qbcyhqdju. shyek q bydwkqwuc tu fhewhqcqçãe vbem-cqjys gku iuhlyk sece rqiu fqhq q shyqçãe te sereb.vey jqcréc kcq tqi fhycuyhqi fhewhqcqtehqi te xqhlqht cqha edu."
  (core/decrypt-text "whqsu ckhhqo xeffuh vey kcq qdqbyijq tu iyijucqi tq cqhydxq tei uijqtei kdytei u qbcyhqdju. shyek q bydwkqwuc tu fhewhqcqçãe vbem-cqjys gku iuhlyk sece rqiu fqhq q shyqçãe te sereb.vey jqcréc kcq tqi fhycuyhqi fhewhqcqtehqi te xqhlqht cqha edu." 42) => (str/lower-case (str "Grace Murray Hopper foi uma analista de sistemas"
                                                                                                                                                                                                                                                                                                        " da Marinha dos Estados Unidos e almirante."
                                                                                                                                                                                                                                                                                                        " Criou a linguagem de programação Flow-Matic que serviu"
                                                                                                                                                                                                                                                                                                        " como base para a criação do COBOL."
                                                                                                                                                                                                                                                                                                        "Foi também uma das primeiras programadoras do Harvard "
                                                                                                                                                                                                                                                                                                        "Mark One.")))

(fact "on count-letters"
  (core/count-letters \a "batata") => 3)

(fact "alphabet is complete"
  core/alphabet => [\a \b \c \d \e \f \g \h \i \j \k \l \m \n \o \p \q \r \s \t \u \v \w \x \y \z])

