/*
 * Copyright 2018-2019 KunMinX
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kunminx.puremusic.ui.page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kunminx.architecture.ui.adapter.SimpleBaseBindingAdapter;
import com.kunminx.puremusic.R;
import com.kunminx.puremusic.bridge.request.MusicRequestViewModel;
import com.kunminx.puremusic.bridge.status.MainViewModel;
import com.kunminx.puremusic.data.bean.TestAlbum;
import com.kunminx.puremusic.databinding.AdapterPlayItemBinding;
import com.kunminx.puremusic.databinding.FragmentMainBinding;
import com.kunminx.puremusic.player.PlayerManager;
import com.kunminx.puremusic.ui.base.BaseFragment;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;

/**
 * Create by KunMinX at 19/10/29
 */
public class MainFragment extends BaseFragment {


    private FragmentMainBinding mBinding;
    private MainViewModel mMainViewModel;
    private MusicRequestViewModel mMusicRequestViewModel;
    private SimpleBaseBindingAdapter<TestAlbum.TestMusic, AdapterPlayItemBinding> mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mMusicRequestViewModel = ViewModelProviders.of(this).get(MusicRequestViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mBinding = FragmentMainBinding.bind(view);
        mBinding.setClick(new ClickProxy());
        mBinding.setVm(mMainViewModel);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMainViewModel.initTabAndPage.set(true);

        mMainViewModel.pageAssetPath.set("summary.html");

        mAdapter = new SimpleBaseBindingAdapter<TestAlbum.TestMusic, AdapterPlayItemBinding>(getContext(), R.layout.adapter_play_item) {
            @Override
            protected void onSimpleBindItem(AdapterPlayItemBinding binding, TestAlbum.TestMusic item, RecyclerView.ViewHolder holder) {
                binding.tvTitle.setText(item.getTitle());
                binding.tvArtist.setText(item.getArtist().getName());
                Glide.with(binding.ivCover.getContext()).load(item.getCoverImg()).into(binding.ivCover);
                int currentIndex = PlayerManager.getInstance().getAlbumIndex();
                binding.ivPlayStatus.setIcon(currentIndex == holder.getAdapterPosition()
                        ? MaterialDrawableBuilder.IconValue.MUSIC_NOTE : null);
                binding.getRoot().setOnClickListener(v -> {
                    PlayerManager.getInstance().playAudio(holder.getAdapterPosition());
                });
            }
        };

        mBinding.rv.setAdapter(mAdapter);

        PlayerManager.getInstance().getChangeMusicLiveData().observe(this, changeMusic -> {
            mAdapter.notifyDataSetChanged();
        });

        mMusicRequestViewModel.getFreeMusicsLiveData().observe(this, musicAlbum -> {
            if (musicAlbum != null && musicAlbum.getMusics() != null) {
                mAdapter.setList(musicAlbum.getMusics());
                mAdapter.notifyDataSetChanged();

                if (PlayerManager.getInstance().getAlbum() == null ||
                        !PlayerManager.getInstance().getAlbum().getAlbumId().equals(musicAlbum.getAlbumId())) {
                    PlayerManager.getInstance().loadAlbum(musicAlbum);
                }
            }
        });

        if (PlayerManager.getInstance().getAlbum() == null) {
            mMusicRequestViewModel.requestFreeMusics();
        } else {
            mAdapter.setList(PlayerManager.getInstance().getAlbum().getMusics());
            mAdapter.notifyDataSetChanged();
        }
    }

    public class ClickProxy {

        public void openMenu() {
            mSharedViewModel.openOrCloseDrawer.setValue(true);
        }

        public void search() {
            nav().navigate(R.id.action_mainFragment_to_searchFragment);
        }
    }

}
