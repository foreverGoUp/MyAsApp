package com.kc.TemplateCode;

import com.kc.base.BasePresenter;

public class TemplatePresenter extends BasePresenter {

    private IActivityNameView mListener;

    public TemplatePresenter(IActivityNameView listener) {
        mListener = listener;
    }


    public interface IActivityNameView {

    }

    @Override
    public void destroy() {
        super.destroy();
        mListener = null;
    }

}
