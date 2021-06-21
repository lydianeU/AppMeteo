package fr.afpa.appmeteo.utils

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.preference.DialogPreference
import fr.afpa.appmeteo.R


class TimerPreference @JvmOverloads constructor(
        context: Context?,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = R.attr.preferenceStyle,
        defStyleRes: Int = defStyleAttr
) : DialogPreference(context, attrs, defStyleAttr, defStyleRes) {


    var time: String = DEFAULT_VALUE
        set(value) {
            field = value
            // Save to SharedPreferences
            persistString(value)
        }


    //  Si il y a une valeur par défaut dans le composant (XML) on la récupère sinon on prend DEFAULT_VALUE
    override fun onGetDefaultValue(a: TypedArray?, index: Int): String {
        return a?.getString(index) ?: DEFAULT_VALUE
    }

    override fun getDialogLayoutResource() = R.layout.preference_timer

    override fun onSetInitialValue(defaultValue: Any?) {
        // Si on peut restaurer une valeur, on la prend, sinon on prend DEFAULT_VALUE
        time = getPersistedString((defaultValue as? String) ?: DEFAULT_VALUE)
    }

    companion object {
        private const val DEFAULT_VALUE = "00:00"
    }
}