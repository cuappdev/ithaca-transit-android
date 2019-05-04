package ithaca_transit.android.cornellappdev.com.ithaca_transit;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.MenuItem;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {
    private DrawerLayout mAboutView;
    private NavigationView mAboutMenu;
    private TextView mAboutMenuHeader;
    private ImageView mMenuButton;
//    private Button onboarding;
    private Button feedback;
    private Button website;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        mAboutView = this.findViewById(R.id.about_view);
        mAboutMenu = this.findViewById(R.id.about_menu);

        View headerView = mAboutMenu.getHeaderView(0);
        mAboutMenuHeader = headerView.findViewById(R.id.menu_header);

        mMenuButton = this.findViewById(R.id.about_menu_button);

//        onboarding = this.findViewById(R.id.about_onboarding);
        feedback = this.findViewById(R.id.about_feedback);
        website = this.findViewById(R.id.about_website);

        setUpMenu();
        makeHereClickable();
        makeButtonsClickable();
    }

    private void setUpMenu() {
        final View parent = (View) mMenuButton.getParent();
        parent.post(() -> {
                final Rect rect = new Rect();
                mMenuButton.getHitRect(rect);
                rect.top -= 100;    // increase top hit area
                rect.left -= 100;   // increase left hit area
                rect.bottom += 100; // increase bottom hit area
                rect.right += 100;  // increase right hit area
                parent.setTouchDelegate(new TouchDelegate(rect , mMenuButton));
            }
        );

        mMenuButton.setOnClickListener((View v) -> mAboutView.openDrawer(mAboutMenu));

        mAboutMenuHeader.setOnClickListener((View v) -> {
                mAboutView.closeDrawer(mAboutMenu);
                Intent goHome = new Intent(AboutActivity.this, MainActivity.class);
                startActivity(goHome);
            }
        );

        mAboutView.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        if (slideOffset < 0.4) {}
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {}

                    @Override
                    public void onDrawerClosed(View drawerView) {}

                    @Override
                    public void onDrawerStateChanged(int newState) {}
                }
        );

        mAboutMenu.setNavigationItemSelectedListener((MenuItem item) -> {
                switch (item.toString()) {
                    case "Send Feedback":
                        sendFeedback();
                        break;
                    default:
                        System.out.println("Not handled");
                }
                mAboutView.closeDrawer(mAboutMenu);
                return true;
            }
        );
    }

    private void makeHereClickable() {
        SpannableString ss = new SpannableString(getResources().getString(R.string.about_proj_team));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(getResources().getString(R.string.appdev_google_play)));
                intent.setPackage("com.android.vending");
                startActivity(intent);
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
            }
        };
        ss.setSpan(clickableSpan, ss.toString().indexOf("here"),
                ss.toString().indexOf("here") + 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        TextView textView = findViewById(R.id.about_proj_team);
        textView.setText(ss);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setHighlightColor(Color.TRANSPARENT);
    }

    private void makeButtonsClickable() {
        // TODO: Complete onboarding button later in development
//        onboarding.setOnClickListener((View v) -> {
//
//            }
//        );

        feedback.setOnClickListener((View v) -> sendFeedback());

        website.setOnClickListener((View v) -> {
                Intent goToWebsite = new Intent(Intent.ACTION_VIEW);
                goToWebsite.setData(Uri.parse(getResources().getString(R.string.appdev_website)));
                startActivity(goToWebsite);
            }
        );
    }

    private void sendFeedback() {
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        String version = getResources().getString(R.string.version_not_found);

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                new String[]{"ithacatransit@cornellappdev.com"});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                "Ithaca Transit Feedback " + version);
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");

        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }
}
