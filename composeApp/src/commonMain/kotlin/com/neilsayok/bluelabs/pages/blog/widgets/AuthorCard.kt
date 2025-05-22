package com.neilsayok.bluelabs.pages.blog.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.neilsayok.bluelabs.common.constants.DEFAULT_IMAGE_SQUARE
import com.neilsayok.bluelabs.data.bloglist.AuthorLoadedData
import com.neilsayok.bluelabs.data.documents.Bio
import com.neilsayok.bluelabs.data.documents.Description
import com.neilsayok.bluelabs.data.documents.ImgUrl
import com.neilsayok.bluelabs.data.documents.Joined
import com.neilsayok.bluelabs.data.documents.Location
import com.neilsayok.bluelabs.data.documents.Name
import com.neilsayok.bluelabs.data.documents.Pass
import com.neilsayok.bluelabs.data.documents.ProfileFields
import com.neilsayok.bluelabs.data.documents.Uid
import com.neilsayok.bluelabs.util.toReadableDate
import de.drick.compose.hotpreview.HotPreview

@HotPreview
@Composable
fun AuthorCardPreview() {
    AuthorCard(
        author = AuthorLoadedData(
            bio = Bio("asdasdas"),
            description = Description("adasdwqsa"),
            imgUrl = ImgUrl(DEFAULT_IMAGE_SQUARE),
            joined = Joined("2022-06-17T18:30:00.422Z"),
            location = Location("adsadsa"),
            name = Name("Sayok Dey Majumder"),
            pass = Pass("asdsadas"),
            profiles = ProfileFields(),
            uid = Uid("asdasd")
        ), postedOn = "2022-06-17T18:30:00.422Z"
    )
}


@Composable
fun AuthorCard(author: AuthorLoadedData?, postedOn: String?) {
    Row(modifier = Modifier.padding(horizontal = 16.dp)) {
        Card(modifier = Modifier.size(38.dp), shape = CircleShape) {
            AsyncImage(
                model = author?.imgUrl?.stringValue ?: DEFAULT_IMAGE_SQUARE,
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
        }

        Spacer(modifier = Modifier.size(8.dp))

        Column {
            Text(
                "${author?.name?.stringValue}".uppercase(),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                "Posted On: ${postedOn?.toReadableDate()}",
                fontWeight = FontWeight.Thin,
                style = MaterialTheme.typography.bodySmall

            )
        }
    }
}