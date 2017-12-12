package com.cfp.muaavin.ui;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.appTheme)/*Color.parseColor("#3b5998")*/);
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
        setContentView(R.layout.activity_about);

        String htmlText = "<html><body style=\"text-align:justify\"> <font size=\"3\" color=\"#000000\" face=\"sans-serif\"> %s </font> </body></Html>";
        String aboutData = "<b>Muavin, community standards</b> <br><br>" +
                "Muavin is a community based application and depends on the support of its community members to ensure that it operates as a safe, secure and friendly space. We welcome you to the Muavin community, as a community member please keep these simple guidelines in mind: " +
                "<br>" +
                "<br><b>Do</b> <br>" +
                "Respect each other – The whole point of this community is to create an alliance among people who can network together to fight back against online violence. This is a shared space and mutual respect is an essential factor. <br>" +
                "Stay focused – This is not a social media platform to post cat videos and funny memes. We love those too, but they are best shared on Facebook, Snapchat and the dozens other social media platforms out there. Muavin is here for a specific purpose, make sure the discussions and comments stay focused on that. <br>" +
                "Engage – The community develops only if all members are participating. Engage with others on the discussion forums, add your voice and voice your opinions. Discussions, even those that include disagreements, can lead to solutions. <br>" +
                "Alert us – if you see anyone misusing or abusing the application. As community members we all have the shared responsibility of ensuring that Muavin continues to operate as a safe space.<br>" +
                "<br>" +
                "<b>Don’t</b> <br>" +
                "Be abusive or hateful – Differences of opinion are natural and even necessary for healthy discussions. But, disagree respectfully without restoring to personal attacks and abuses. Abusive, profane language and hate speech against other users be removed. <br>" +
                "Troll – Trolling i.e. deliberately trying to disrupt a discussion by posting statements or comments clearly meant to detract discussions or ridicule users is not acceptable. <br>" +
                "Violate other’s privacy – Muavin is a community space, but one with a specific focus that has its own sensitivities. Do not take screenshots of discussions or other people’s posts and share them on your own social media. <br>" +
                "Spam – You can discuss anything and everything as long as it is clearly linked to the focus areas of discussion groups, but please don’t use forums to post advertisements, unconfirmed forwarded messages or other spam material. <br>" +
                "<br>" +
                "<b>Remember</b> <br>" +
                "This is a pilot version – we depend on your support and your active participation to help us improve this application and make it a more efficient tool of support for women and other groups.  We reserve the right to make editorial decisions regarding submitted comments, and discussions on the discussion forums, including but not limited to removal of comments. <br>" +
                "<br>" +
                "We welcome your feedback through the application, or through our other social media channels. Use our Facebook Page to discuss how this application can be improved or send us direct feedback through Media Matters for Democracy’s Facebook page or Twitter. Thank you for sharing this journey with us, we look forward to improving Muavin with you. ";

        WebView viewAbout = (WebView) findViewById(R.id.webview);
        viewAbout.loadData(String.format(htmlText, aboutData), "text/html", "utf-8");
        viewAbout.setBackgroundColor(0);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_web, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack();
                } else super.onBackPressed();
                break;
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
