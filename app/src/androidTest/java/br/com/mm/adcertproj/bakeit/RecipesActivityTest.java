package br.com.mm.adcertproj.bakeit;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.mm.adcertproj.bakeit.activity.RecipesActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;

@RunWith(AndroidJUnit4.class)
public class RecipesActivityTest {
    // Those should be the recipes, in order, during development process.
    private static final String[] RECIPES_NAME =
            {"Nutella Pie", "Brownies", "Yellow Cake", "Cheesecake"};
    private static final String[] RECIPES_SERVINGS =
            {"for 8 servings", "for 8 servings", "for 8 servings", "for 8 servings"};
    private static final int RECIPE_INDEX = 1;
    private static final String RECIPE_STEP = "Recipe Introduction";
    private static final String RECIPE_STEP_INSTRUCTIONS = "Recipe Introduction";
    private static final String[] RECIPE_NEXT_STEP =
            {"Starting prep", "Starting prep", "Starting prep", "Starting prep."};

    private IdlingResource mIdlingResource;

    @Rule
    public ActivityTestRule<RecipesActivity> mActivityTestRule =
            new ActivityTestRule<>(RecipesActivity.class);

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        Espresso.registerIdlingResources(mIdlingResource);
    }

    @Test
    public void clickRecyclerViewItem_OpensBakeITActivity() {
        // There is no easy way to test RecyclerView on API 25 (maybe 23 and backwards with espresso-contrib)
        onView(allOf(withId(R.id.tv_recipe_name),withText(RECIPES_NAME[RECIPE_INDEX])))
                .perform(click());

        onView(withId(R.id.tv_recipe_name))
                .check(matches(withText(RECIPES_NAME[RECIPE_INDEX])));
        onView(withId(R.id.tv_recipe_details))
                .check(matches(withText(RECIPES_SERVINGS[RECIPE_INDEX])));
    }

    @Test
    public void clickRecyclerViewItem_clickStepItem_OpenDetailsFragment() {
        onView(allOf(withId(R.id.tv_recipe_name),withText(RECIPES_NAME[RECIPE_INDEX])))
                .perform(click());

        onView(allOf(withId(R.id.tv_step),withText(RECIPE_STEP)))
                .perform(scrollTo(), click());

        onView(withId(R.id.tv_step_name))
                .check(matches(withText(RECIPE_STEP)));
        onView(withId(R.id.tv_step_instructions))
                .check(matches(withText(RECIPE_STEP_INSTRUCTIONS)));
    }

    @Test
    public void clickRV_clickStep_clickNext_OpenNextStepFragment() {
        onView(allOf(withId(R.id.tv_recipe_name),withText(RECIPES_NAME[RECIPE_INDEX])))
                .perform(click());

        onView(allOf(withId(R.id.tv_step),withText(RECIPE_STEP)))
                .perform(scrollTo(), click());

        onView(withId(R.id.button_next)).perform(scrollTo(), click());

        onView(withId(R.id.tv_step_name))
                .check(matches(withText(RECIPE_NEXT_STEP[RECIPE_INDEX])));
    }

    @After
    public void unregisterIdlingResource() {
        if(mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }
}
