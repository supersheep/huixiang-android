package in.spud.huixiang;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.devspark.progressfragment.ProgressFragment;

import java.util.List;

import in.spud.huixiang.model.Piece;
import in.spud.huixiang.util.ProgressDialogFragment;
import in.spud.huixiang.util.Utility;


/**
 * A simple {@link Fragment} subclass.
 * to handle interaction events.
 * Use the {@link MineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MineFragment extends ProgressFragment implements
        Response.Listener<List<Piece>>,
        Response.ErrorListener,
        AdapterView.OnItemClickListener {

    private long mCurrentPage = 1;
    private List<Piece> mPieces;
    private Piece mSelectedPiece;
    private View mContentView;
    private ListView mListView;
    private FavPieceAdapter mListViewAdapter;
    private ProgressDialogFragment mProgressDlg;


    public static MineFragment newInstance() {

        MineFragment fragment = new MineFragment();
        return fragment;
    }

    public MineFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fav_view_content, null);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mListView = (ListView) mContentView.findViewById(R.id.pieces_list);
        mListViewAdapter = new FavPieceAdapter(getActivity(), mPieces);
        mListView.setAdapter(mListViewAdapter);

        mListView.setOnItemClickListener(this);

        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        setContentView(mContentView);
        setEmptyText("收藏过的片段会出现在这里 :)");


        if (mPieces == null || mPieces.size() == 0) {
            setContentShown(false);
            HuixiangApp.get().getApi().getFavList(0, this, this);
        } else {
            setContentShown(true);
        }

        super.onActivityCreated(savedInstanceState);
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
    public void onErrorResponse(VolleyError volleyError) {

        volleyError.printStackTrace();
    }

    @Override
    public void onResponse(List<Piece> pieces) {
        boolean isContentEmpty = (pieces == null || pieces.size() == 0);

        setContentShown(true);

        if (mCurrentPage == 1) {
            mPieces = pieces;
            setContentEmpty(isContentEmpty);
        } else {
            mPieces.addAll(pieces);
        }
        mListViewAdapter.setPieces(mPieces);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        final Piece piece = (Piece) mListViewAdapter.getItem(position);

        if (piece != null) {
            mSelectedPiece = piece;
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("茴香");
            builder.setItems(R.array.fav_action_list, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            unFav(piece);
                            break;
                        case 1:
                            postWeibo(piece);
                            break;
                        case 2:
                            Utility.share(getActivity(), piece.getContent());
                            break;
                    }
                }
            });

            builder.create().show();

        }


    }

    private void postWeibo(Piece piece) {
    }

    private void unFav(Piece piece) {
        if (piece != null) {
            mProgressDlg = ProgressDialogFragment.newInstance();
            mProgressDlg.setMessage(getString(R.string.unfav_progress));
            mProgressDlg.setRequest(HuixiangApp.get().getApi().unfav(piece.getId(), new UnFavListener(), new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (mProgressDlg != null && mProgressDlg.isAdded()) {
                        mProgressDlg.dismiss();
                    }
                    Toast.makeText(getActivity(), R.string.unfav_failed, Toast.LENGTH_LONG).show();
                }
            }));
            mProgressDlg.show(getFragmentManager(), "unfav");
        }

    }

    class UnFavListener implements Response.Listener {

        @Override
        public void onResponse(Object o) {
            if (mProgressDlg != null && mProgressDlg.isAdded()) {
                mProgressDlg.dismiss();
            }

            Toast.makeText(getActivity(), R.string.unfav_success, Toast.LENGTH_LONG).show();
            mListViewAdapter.removePiece(mSelectedPiece);
        }
    }

    class FavPieceAdapter extends BaseAdapter {

        private List<Piece> mPieces;
        private LayoutInflater mLayoutInflater;

        public FavPieceAdapter(Context context, List<Piece> pieces) {
            mLayoutInflater = LayoutInflater.from(context);
            mPieces = pieces;
        }

        @Override
        public int getCount() {
            return mPieces == null ? 0 : mPieces.size();
        }

        @Override
        public Object getItem(int position) {
            return mPieces == null ? null : mPieces.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void setPieces(List<Piece> pieces) {
            mPieces = pieces;
            notifyDataSetChanged();
        }

        public void removePiece(Piece piece) {
            if (mPieces != null && piece != null) {
                for (Piece p : mPieces) {
                    if (p.getId() == piece.getId()) {
                        mPieces.remove(piece);
                        notifyDataSetChanged();
                        return;
                    }
                }
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            // 使用ViewHolder可以减少xml解析查询，提高性能
            ViewHolder viewHolder = null;
            int resource = R.layout.fav_piece_item;

            if (convertView == null) {
                view = mLayoutInflater.inflate(resource, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.piece = (TextView) view.findViewById(R.id.huixiang_piece);
                view.setTag(viewHolder);
            } else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }
            bindView(position, viewHolder);
            return view;
        }

        private void bindView(int position, ViewHolder viewHolder) {
            Piece piece = (Piece) getItem(position);
            viewHolder.piece.setText(piece.getContent());
        }
    }

    class ViewHolder {
        TextView piece;
    }
}
