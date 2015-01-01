package in.spud.huixiang;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.devspark.progressfragment.ProgressFragment;

import java.util.List;

import in.spud.huixiang.model.Piece;

import com.android.volley.Response.Listener;
import com.android.volley.Response.ErrorListener;

/**
 * Use the {@link PieceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PieceFragment extends ProgressFragment implements Listener<List<Piece>>, ErrorListener, View.OnClickListener {

    private String TAG = PieceFragment.class.getName();
    private View mContentView;
    private List<Piece> mPieces;
    private Request mInFlightRequest;

    private boolean toggleHideBlueView = true;

    private int mCurrentIndex = 0;

    private TextView mPieceView;

    public static final String EXTRA_MESSAGE = "in.spud.huixiang.MESSAGE";

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PieceFragment.
     */
    public static PieceFragment newInstance() {
        PieceFragment fragment = new PieceFragment();
        return fragment;
    }

    public PieceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContentView = inflater.inflate(R.layout.piece_view_content, null);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
//        mContentView.findViewById(R.id.button_press_me_baby).setOnClickListener(this);
//        mContentView.findViewById(R.id.button_send).setOnClickListener(this);

        mPieceView = (TextView) mContentView.findViewById(R.id.huixiang_piece);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setContentView(mContentView);
        // Setup text for empty content
        setEmptyText("木有内容");

        if (mPieces == null || mPieces.size() == 0) {
            setContentShown(false);
            mInFlightRequest = HuixiangApp.get().getApi().getPieces(this, this);
        } else {
            setContentShown(true);
            updatePiece();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {

        Activity ac = getActivity();

        switch(v.getId()){
            case R.id.button_press_me_baby:

                final View blueView = ac.findViewById(R.id.just_a_blue_view);

                if(toggleHideBlueView){
                    blueView.animate()
                            .alpha(0)
                            .setDuration(200)
                            .setListener(new AnimatorListenerAdapter(){
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                }
                            });
                }else{
                    blueView.setVisibility(View.VISIBLE);
                    blueView.animate()
                            .alpha(1)
                            .setDuration(200)
                            .setListener(null);
                }

                toggleHideBlueView = !toggleHideBlueView;
                break;
            case R.id.search_button:
                EditText editText = (EditText) ac.findViewById(R.id.edit_message);
                Intent intent = new Intent(ac, DisplayMessageActivity.class);
                intent.putExtra(EXTRA_MESSAGE, editText.getText().toString());
                startActivity(intent);
                break;
        }
    }


    @Override
    public void onErrorResponse(VolleyError volleyError) {
        volleyError.printStackTrace();
    }

    @Override
    public void onResponse(List<Piece> pieces) {
        setContentShown(true);
        if (mPieces == null) {
            mPieces = pieces;
        } else {
            synchronized (mPieces) {
                mPieces.addAll(pieces);
            }
        }
        updatePiece();
        mInFlightRequest = null;
    }

    private void showNextPiece() {
        mCurrentIndex++;
        updatePiece();
    }


    private void updatePiece() {
        final List<Piece> pieces = mPieces;
        Piece piece = null;

        synchronized (pieces) {
            if (mCurrentIndex > pieces.size() - 10 && mInFlightRequest == null) {
                mInFlightRequest = HuixiangApp.get().getApi().getPieces(this, this);
            }

            if (mCurrentIndex < pieces.size()) {
                piece = pieces.get(mCurrentIndex);
                mPieceView.setText(piece.getContent());

//                AnimationUtil.fadeInItem(mPieceView, 1000, 0, View.VISIBLE, null);
            }
        }
    }
}
