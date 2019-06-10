package com.techyourchance.testdoublesfundamentals.exercise4;

import com.techyourchance.testdoublesfundamentals.example4.networking.NetworkErrorException;
import com.techyourchance.testdoublesfundamentals.exercise4.networking.UserProfileHttpEndpointSync;
import com.techyourchance.testdoublesfundamentals.exercise4.users.User;
import com.techyourchance.testdoublesfundamentals.exercise4.users.UsersCache;

import org.jetbrains.annotations.Nullable;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.techyourchance.testdoublesfundamentals.exercise4.ExerciseSolution4.FULL_NAME;
import static com.techyourchance.testdoublesfundamentals.exercise4.ExerciseSolution4.IMAGE_URL;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class FetchUserProfileUseCaseSyncTest {

    public static final String USER_ID = "userId";

    FetchUserProfileUseCaseSync SUT;
    UserProfileHttpEndpointSyncHelper mUserProfileHttpEndpointSyncHelper;
    UsersCacheHelper mUsersCacheHelper;

    @Before
    public void setUp() throws Exception {
        mUserProfileHttpEndpointSyncHelper = new UserProfileHttpEndpointSyncHelper();
        mUsersCacheHelper = new UsersCacheHelper();
        SUT = new FetchUserProfileUseCaseSync(mUserProfileHttpEndpointSyncHelper, mUsersCacheHelper);
    }

    //UserId pass to Endpoint
    @Test
    public void loginSync_success_userIdPassedToEndpoint() throws Exception {
        SUT.fetchUserProfileSync(USER_ID);
        assertThat(mUserProfileHttpEndpointSyncHelper.mUserId, is(USER_ID));
    }

    //Login success: user's auth token must be catched.
    @Test
    public void loginSync_success_userTokenCatch() throws Exception {
        SUT.fetchUserProfileSync(USER_ID);
        User usersCache = mUsersCacheHelper.getUser(USER_ID);
        assertThat(usersCache.getUserId(), is(USER_ID));
        assertThat(usersCache.getFullName(), is(FULL_NAME));
        assertThat(usersCache.getImageUrl(), is(IMAGE_URL));
    }

    //Login fail: login token is not change.
    @Test
    public void loginSync_fail_userTokenNoChange() throws Exception {
        mUserProfileHttpEndpointSyncHelper.mIsGeneralError = true;
        SUT.fetchUserProfileSync(USER_ID);
        assertThat(mUsersCacheHelper.getUser(USER_ID), is(nullValue()));
    }

    @Test
    public void fetchUserProfileSync_authError_userNotCached() throws Exception {
        mUserProfileHttpEndpointSyncHelper.mIsAuthError = true;
        SUT.fetchUserProfileSync(USER_ID);
        assertThat(mUsersCacheHelper.getUser(USER_ID), is(nullValue()));
    }

    @Test
    public void fetchUserProfileSync_serverError_userNotCached() throws Exception {
        mUserProfileHttpEndpointSyncHelper.mIsServerError = true;
        SUT.fetchUserProfileSync(USER_ID);
        assertThat(mUsersCacheHelper.getUser(USER_ID), is(nullValue()));
    }

    //Login success: return success.
    @Test
    public void loginSync_success_successReturned() throws Exception {
        FetchUserProfileUseCaseSync.UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
        assertThat(result, is(FetchUserProfileUseCaseSync.UseCaseResult.SUCCESS));
    }

    @Test
    public void loginSync_serverError_failureReturned() throws Exception {
        mUserProfileHttpEndpointSyncHelper.mIsServerError = true;
        FetchUserProfileUseCaseSync.UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
        assertThat(result, is(FetchUserProfileUseCaseSync.UseCaseResult.FAILURE));
    }

    @Test
    public void loginSync_authError_failureReturned() throws Exception {
        mUserProfileHttpEndpointSyncHelper.mIsAuthError = true;
        FetchUserProfileUseCaseSync.UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
        assertThat(result, is(FetchUserProfileUseCaseSync.UseCaseResult.FAILURE));
    }

    //Network: return network error.
    @Test
    public void loginSync_generalError_failureReturned() throws Exception {
        mUserProfileHttpEndpointSyncHelper.mIsGeneralError = true;
        FetchUserProfileUseCaseSync.UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
        assertThat(result, is(FetchUserProfileUseCaseSync.UseCaseResult.FAILURE));
    }

    private static class UserProfileHttpEndpointSyncHelper implements  UserProfileHttpEndpointSync {
        public String mUserId = "";
        public boolean mIsGeneralError;
        public boolean mIsAuthError;
        public boolean mIsServerError;
        public boolean mIsNetworkError;

        @Override
        public EndpointResult getUserProfile(String userId) throws NetworkErrorException {
            mUserId = userId;
            if (mIsGeneralError) {
                return new EndpointResult(EndpointResultStatus.GENERAL_ERROR, "", "", "");
            } else if (mIsAuthError) {
                return new EndpointResult(EndpointResultStatus.AUTH_ERROR, "", "", "");
            }  else if (mIsServerError) {
                return new EndpointResult(EndpointResultStatus.SERVER_ERROR, "", "", "");
            } else if (mIsNetworkError) {
                throw new NetworkErrorException();
            } else {
                return new EndpointResult(EndpointResultStatus.SUCCESS, USER_ID, FULL_NAME, IMAGE_URL);
            }
        }
    }

    private static class UsersCacheHelper implements  UsersCache {

        private List<User> mUsers = new ArrayList<>(1);

        @Override
        public void cacheUser(User user) {
            User existingUser = getUser(user.getUserId());
            if (existingUser != null) {
                mUsers.remove(existingUser);
            }
            mUsers.add(user);
        }

        @Override
        @Nullable
        public User getUser(String userId) {
            for (User user : mUsers) {
                if (user.getUserId().equals(userId)) {
                    return user;
                }
            }
            return null;
        }
    }
}