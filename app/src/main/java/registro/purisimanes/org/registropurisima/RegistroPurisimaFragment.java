package registro.purisimanes.org.registropurisima;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * A placeholder fragment containing a simple view.
 */
public class RegistroPurisimaFragment extends Fragment {

    private static String nombre;
    private static String apellidos;
    private static String email;

    private static EditText etNombre;
    private static EditText etApellidos;
    private static EditText etEmail;

    public RegistroPurisimaFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_registro_purisima, container, false);
        etNombre = (EditText) v.findViewById(R.id.etNombre);
        etApellidos = (EditText) v.findViewById(R.id.etApellidos);
        etEmail = (EditText) v.findViewById(R.id.etEmail);
        return v;
    }
}
