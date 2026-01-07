package pt.ips.pointsystem.model

enum class PicagemType {
    ENTRADA {
        override fun toString(): String {
            return "ENTRADA"
        }
    },
    PAUSA_INICIO {
        override fun toString(): String {
            return "PAUSA_INICIO"
        }
    },
    PAUSA_FIM {
        override fun toString(): String {
            return "PAUSA_FIM"
        }
    },
    SAIDA {
        override fun toString(): String {
            return "SAIDA"
        }
    }
}