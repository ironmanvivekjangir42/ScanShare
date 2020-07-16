package com.scanlibrary;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by jhansi on 29/03/15.
 */
public class ResultFragment extends Fragment {

    private View view;
    private ImageView scannedImageView;
    private Button doneButton;
    private Button addButton;
    private Bitmap original;
    private Button originalButton;
    private Button MagicColorButton;
    private Button grayModeButton;
    private Button bwButton;
    private Button rotateR;
    private Button rotateL;
    private Bitmap transformed;
    private static ProgressDialogFragment progressDialogFragment;



    public ResultFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.result_layout, null);
        init();
        return view;
    }

    private void init() {
        scannedImageView = (ImageView) view.findViewById(R.id.scannedImage);
        originalButton = (Button) view.findViewById(R.id.original);
        originalButton.setOnClickListener(new OriginalButtonClickListener());
        MagicColorButton = (Button) view.findViewById(R.id.magicColor);
        MagicColorButton.setOnClickListener(new MagicColorButtonClickListener());
        grayModeButton = (Button) view.findViewById(R.id.grayMode);
        grayModeButton.setOnClickListener(new GrayButtonClickListener());
        bwButton = (Button) view.findViewById(R.id.BWMode);
        bwButton.setOnClickListener(new BWButtonClickListener());
        final Bitmap bitmap = getBitmap();
        setScannedImage(bitmap);
        doneButton = (Button) view.findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new DoneButtonClickListener());
        addButton = (Button) view.findViewById(R.id.addimage);
        addButton.setOnClickListener(new addButtonClickListener());

        rotateL = (Button) view.findViewById(R.id.rotateL);
        rotateR = (Button) view.findViewById(R.id.rotateR);

        rotateL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transformed = RotateBitmap(transformed,90);
                scannedImageView.setImageBitmap(transformed);

            }
        });
        rotateR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transformed = RotateBitmap(transformed,270);
                scannedImageView.setImageBitmap(transformed);
            }
        });

    }

    private Bitmap getBitmap() {
        Uri uri = getUri();
        try {
            original = Utils.getBitmap(getActivity(), uri);
            getActivity().getContentResolver().delete(uri, null, null);
            return original;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Uri getUri() {
        Uri uri = getArguments().getParcelable(ScanConstants.SCANNED_RESULT);
        return uri;
    }

    public void setScannedImage(Bitmap scannedImage) {
        scannedImageView.setImageBitmap(scannedImage);
    }

    private class DoneButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            showProgressDialog(getResources().getString(R.string.loading));
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Intent data = new Intent();
                        Bitmap bitmap = transformed;
                        if (bitmap == null) {
                            bitmap = original;
                        }
                        Uri uri = Utils.getUri(getActivity(), bitmap);
                        data.putExtra(ScanConstants.SCANNED_RESULT, uri);
                        data.putExtra(ScanConstants.SCANNED_RETURN_STATE, 0);
                        getActivity().setResult(Activity.RESULT_OK, data);
                        original.recycle();
                        System.gc();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dismissDialog();
                                getActivity().finish();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private class addButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            showProgressDialog(getResources().getString(R.string.loading));
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Intent data = new Intent();
                        Bitmap bitmap = transformed;
                        if (bitmap == null) {
                            bitmap = original;
                        }
                        Uri uri = Utils.getUri(getActivity(), bitmap);
                        data.putExtra(ScanConstants.SCANNED_RESULT, uri);
                        data.putExtra(ScanConstants.SCANNED_RETURN_STATE, 1);
                        getActivity().setResult(Activity.RESULT_OK, data);
                        original.recycle();
                        System.gc();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dismissDialog();
                                getActivity().finish();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private class BWButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(final View v) {
            showProgressDialog(getResources().getString(R.string.applying_filter));
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        transformed = AdaptiveThreshold(original);
                    } catch (final OutOfMemoryError e) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                transformed = original;
                                scannedImageView.setImageBitmap(original);
                                e.printStackTrace();
                                dismissDialog();
                                onClick(v);
                            }
                        });
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            scannedImageView.setImageBitmap(transformed);
                            dismissDialog();
                        }
                    });
                }
            });
        }
    }



    private class MagicColorButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(final View v) {
            showProgressDialog(getResources().getString(R.string.applying_filter));
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        transformed = ((ScanActivity) getActivity()).getMagicColorBitmap(original);
                    } catch (final OutOfMemoryError e) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                transformed = original;
                                scannedImageView.setImageBitmap(original);
                                e.printStackTrace();
                                dismissDialog();
                                onClick(v);
                            }
                        });
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            scannedImageView.setImageBitmap(transformed);
                            dismissDialog();
                        }
                    });
                }
            });
        }
    }

    private class OriginalButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            try {
                showProgressDialog(getResources().getString(R.string.applying_filter));
                transformed = original;
                scannedImageView.setImageBitmap(original);
                dismissDialog();
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                dismissDialog();
            }
        }
    }

    private class GrayButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(final View v) {
            showProgressDialog(getResources().getString(R.string.applying_filter));
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        transformed = ((ScanActivity) getActivity()).getGrayBitmap(original);
                    } catch (final OutOfMemoryError e) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                transformed = original;
                                scannedImageView.setImageBitmap(original);
                                e.printStackTrace();
                                dismissDialog();
                                onClick(v);
                            }
                        });
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            scannedImageView.setImageBitmap(transformed);
                            dismissDialog();
                        }
                    });
                }
            });
        }
    }

    protected synchronized void showProgressDialog(String message) {
        if (progressDialogFragment != null && progressDialogFragment.isVisible()) {
            // Before creating another loading dialog, close all opened loading dialogs (if any)
            progressDialogFragment.dismissAllowingStateLoss();
        }
        progressDialogFragment = null;
        progressDialogFragment = new ProgressDialogFragment(message);
        FragmentManager fm = getFragmentManager();
        progressDialogFragment.show(fm, ProgressDialogFragment.class.toString());
    }

    protected synchronized void dismissDialog() {
        progressDialogFragment.dismissAllowingStateLoss();
    }

    public static Bitmap AdaptiveThreshold(Bitmap bm2)
    {
        Bitmap bm;
        bm=bm2.copy(Bitmap.Config.RGB_565, true);
        final   int width = bm.getWidth();
        final  int height = bm.getHeight();

        int pixel1,pixel2,pixel3,pixel4,A,R;
        int[]  pixels;
        pixels = new int[width*height];
        bm.getPixels(pixels,0,width,0,0,width,height);
        int size=width*height;
        int s=width/8;
        int s2=s>>1;
        double t=0.15;
        double it=1.0-t;
        int []integral= new int[size];
        int []threshold=new int[size];
        int i,j,diff,x1,y1,x2,y2,ind1,ind2,ind3;
        int sum=0;
        int ind=0;
        while(ind<size)
        {
            sum+=pixels[ind] & 0xFF;
            integral[ind]=sum;
            ind+=width;
        }
        x1=0;
        for(i=1;i<width;++i)
        {
            sum=0;
            ind=i;
            ind3=ind-s2;
            if(i>s)
            {
                x1=i-s;
            }
            diff=i-x1;
            for(j=0;j<height;++j)
            {
                sum+=pixels[ind] & 0xFF;
                integral[ind]=integral[(int)(ind-1)]+sum;
                ind+=width;
                if(i<s2)continue;
                if(j<s2)continue;
                y1=(j<s ? 0 : j-s);
                ind1=y1*width;
                ind2=j*width;

                if (((pixels[ind3]&0xFF)*(diff * (j - y1))) < ((integral[(int)(ind2 + i)] - integral[(int)(ind1 + i)] - integral[(int)(ind2 + x1)] + integral[(int)(ind1 + x1)])*it)) {
                    threshold[ind3] = 0x00;
                } else {
                    threshold[ind3] = 0xFFFFFF;
                }
                ind3 += width;
            }
        }

        y1 = 0;
        for( j = 0; j < height; ++j )
        {
            i = 0;
            y2 =height- 1;
            if( j <height- s2 )
            {
                i = width - s2;
                y2 = j + s2;
            }

            ind = j * width + i;
            if( j > s2 ) y1 = j - s2;
            ind1 = y1 * width;
            ind2 = y2 * width;
            diff = y2 - y1;
            for( ; i < width; ++i, ++ind )
            {

                x1 = ( i < s2 ? 0 : i - s2);
                x2 = i + s2;

                // check the border
                if (x2 >= width) x2 = width - 1;

                if (((pixels[ind]&0xFF)*((x2 - x1) * diff)) < ((integral[(int)(ind2 + x2)] - integral[(int)(ind1 + x2)] - integral[(int)(ind2 + x1)] + integral[(int)(ind1 + x1)])*it)) {
                    threshold[ind] = 0x00;
                } else {
                    threshold[ind] = 0xFFFFFF;
                }
            }
        }
        /*-------------------------------
         * --------------------------------------------*/
        bm.setPixels(threshold,0,width,0,0,width,height);

        return bm;
    }

    public static Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
}