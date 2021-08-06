/*
 *
 *  * Copyright 2021 All rights are reserved by Pi App Studio
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *
 *
 */

package com.piappstudio.travelinsurance.ui.insurance

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.piappstudio.pilibrary.utility.addComma
import com.piappstudio.travelinsurance.R
import com.piappstudio.travelinsurance.common.readJsonFile
import com.piappstudio.travelinsurance.common.theme.Purple700
import com.piappstudio.travelinsurance.common.theme.Teal200
import com.piappstudio.travelinsurance.common.theme.TravelInsuranceTheme
import com.piappstudio.travelinsurance.model.mbo.InsuranceInfoItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class InsuranceFragment : Fragment() {

    private val TAG = InsuranceFragment::class.java.name

    @Inject
    lateinit var insuranceViewModel: InsuranceViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    @ExperimentalMaterialApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setContent {

                TravelInsuranceTheme {
                    val lstInsurance: List<InsuranceInfoItem> by insuranceViewModel.lstInsuranceProvider.observeAsState(
                        initial = emptyList()
                    )
                    renderInsuranceList(lstInsurance = lstInsurance)
                }

            }
        }
    }

    fun initUI() {
        lifecycleScope.launch (Dispatchers.IO) {
            val jsonString = requireActivity().readJsonFile("insurance.json")
            insuranceViewModel.parseJson(jsonString)
        }
    }

    @Composable
    fun renderInsuranceList(lstInsurance: List<InsuranceInfoItem>) {
        LazyColumn(
            Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            items(lstInsurance) { message ->
                itemInsuranceRow(insuranceItem = message, onclick = {
                    Log.d(TAG, "Item clicked")
                    insuranceViewModel.updateSelectedInsuranceInfo(message)
                    findNavController().navigate(R.id.action_insuranceFragment_to_insuranceDetailFragment)
                })
            }
        }
    }

    @Composable
    fun itemInsuranceRow(insuranceItem: InsuranceInfoItem, onclick: () -> Unit) {
        val padding = 16.dp
        val height = 10.dp
        Card(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .fillMaxWidth()
                .clickable { onclick.invoke() },
            elevation = 2.dp,
            backgroundColor = Color.White,
            shape = RoundedCornerShape(corner = CornerSize(16.dp))
        ) {
            Column(
                Modifier
                    .padding(padding)
                    .fillMaxWidth()) {
                Text(
                    text = insuranceItem.supplierName ?: stringResource(R.string.not_available),
                    style = typography.h5
                )
                Spacer(modifier = Modifier.height(height))

                // To design the original and base price
                Box(
                    Modifier
                        .fillMaxWidth()) {
                    Column(
                        Modifier
                            .fillMaxWidth(.5f)
                            .align(Alignment.TopStart)) {
                        Text(
                            text = "Original Price",
                            style = typography.subtitle1
                        )

                        Text(
                            text = insuranceItem.irdaPackagePremium?.addComma() ?: "",
                            textDecoration = TextDecoration.LineThrough,
                            fontStyle = FontStyle.Italic,
                            style = typography.h5,
                            color = Color.Red,
                            fontWeight = FontWeight.Light
                        )
                    }
                    Column(
                        Modifier
                            .fillMaxWidth(.5f)
                            .align(Alignment.TopEnd)) {
                        Text(
                            text = "Offer Price",
                            modifier = Modifier.fillMaxWidth() ,
                            style = typography.subtitle1,
                            textAlign = TextAlign.End

                        )
                        Text(
                            text = insuranceItem.finalPremium?.addComma() ?: "",
                            modifier = Modifier.fillMaxWidth(),
                            fontStyle = FontStyle.Italic,
                            style = typography.h5,
                            color = Purple700,
                            textAlign = TextAlign.End,
                            fontWeight = FontWeight.Black
                        )
                    }


                }
            }
        }
    }

    @Preview
    @Composable
    fun previewitemInsuranceRow() {
        itemInsuranceRow(InsuranceInfoItem(supplierName = "LIC of In", finalPremium = 21300.0, irdaPackagePremium = 25000.0)) {
            
        }
    }

}