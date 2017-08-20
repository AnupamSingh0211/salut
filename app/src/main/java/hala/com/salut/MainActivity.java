package hala.com.salut;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * @author Anupam Singh
 * @version 1.0
 * @since 2017-08-20
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_console);
    }
}
