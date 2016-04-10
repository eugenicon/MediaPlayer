package com.jplayer.media.library;

import com.jplayer.media.file.MediaFile;

import java.util.*;

public class MediaLibrary extends Observable implements List<MediaFile> {

    private List<MediaFile> mediaFiles;

    public MediaLibrary(List<MediaFile> mediaFiles) {
        this.mediaFiles = mediaFiles;
    }

    public List<MediaFile> getContent() {
        return mediaFiles;
    }

    @Override
    public int size() {
        return mediaFiles.size();
    }

    @Override
    public boolean isEmpty() {
        return mediaFiles.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return mediaFiles.contains(o);
    }

    @Override
    public Iterator<MediaFile> iterator() {
        return mediaFiles.iterator();
    }

    @Override
    public Object[] toArray() {
        return mediaFiles.toArray();
    }

    @SuppressWarnings("SuspiciousToArrayCall")
    @Override
    public <T> T[] toArray(T[] a) {
        return mediaFiles.toArray(a);
    }

    @Override
    public boolean add(MediaFile mediaFile) {
        boolean result = mediaFiles.add(mediaFile);
        onChange(mediaFile, Action.ADD);
        return result;
    }

    @Override
    public boolean remove(Object o) {
        boolean remove = mediaFiles.remove(o);
        if (remove) {
            onChange((MediaFile) o, Action.REMOVE);
        }
        return remove;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return mediaFiles.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends MediaFile> c) {
        boolean addAll = mediaFiles.addAll(c);
        onChange(c, Action.ADD);
        return addAll;
    }

    @Override
    public boolean addAll(int index, Collection<? extends MediaFile> c) {
        boolean addAll = mediaFiles.addAll(index, c);
        onChange(c, Action.ADD);
        return addAll;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean removeAll = mediaFiles.removeAll(c);
        if (removeAll) {
            onChange((Collection<? extends MediaFile>) c, Action.REMOVE);
        }
        return removeAll;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        mediaFiles.clear();
        onChange(mediaFiles, Action.REMOVE);
    }

    @Override
    public MediaFile get(int index) {
        return mediaFiles.get(index);
    }

    @Override
    public MediaFile set(int index, MediaFile element) {
        MediaFile removed = mediaFiles.set(index, element);
        onChange(removed, Action.REMOVE);
        onChange(element, Action.ADD);
        return removed;
    }

    @Override
    public void add(int index, MediaFile element) {
        mediaFiles.add(index, element);
        onChange(element, Action.ADD);
    }

    @Override
    public MediaFile remove(int index) {
        MediaFile removed = mediaFiles.remove(index);
        onChange(removed, Action.REMOVE);
        return removed;
    }

    @Override
    public int indexOf(Object o) {
        return mediaFiles.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return mediaFiles.lastIndexOf(o);
    }

    @Override
    public ListIterator<MediaFile> listIterator() {
        return mediaFiles.listIterator();
    }

    @Override
    public ListIterator<MediaFile> listIterator(int index) {
        return mediaFiles.listIterator(index);
    }

    @Override
    public List<MediaFile> subList(int fromIndex, int toIndex) {
        return mediaFiles.subList(fromIndex, toIndex);
    }

    private void onChange(MediaFile mediaFile, Action action) {
        setChanged();
        notifyObservers(new MediaLibraryAction(action, mediaFile));
    }

    private void onChange(Collection<? extends MediaFile> mediaFiles, Action action) {
        setChanged();
        notifyObservers(new MediaLibraryAction(action, mediaFiles));
    }

}
