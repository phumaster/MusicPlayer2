package phu.com.musicplayer;

/**
 * Created by root on 4/1/16.
 */
public class SongDetail {

    private String name;
    private String artist;
    private String albumName;
    private String bitRate;

    public SongDetail() {

    }

    public SongDetail(String _name, String _artist, String _album, String _bitRate) {
        this.name = _name;
        this.artist = _artist;
        this.albumName = _album;
        this.bitRate = _bitRate;
    }

    public String getName() {
        return this.name;
    }

    public String getArtist() {
        return this.artist;
    }

    public String getAlbumName() {
        return this.albumName;
    }

    public String getBitRate() {
        return this.bitRate;
    }

}